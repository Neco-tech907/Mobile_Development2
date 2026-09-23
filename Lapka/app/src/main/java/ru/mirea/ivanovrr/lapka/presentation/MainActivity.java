package ru.mirea.ivanovrr.lapka.presentation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ru.mirea.ivanovrr.lapka.R;
import ru.mirea.ivanovrr.lapka.di.ServiceLocator;
import ru.mirea.ivanovrr.lapka.domain.models.Breed;
import ru.mirea.ivanovrr.lapka.domain.models.Encounter;
import ru.mirea.ivanovrr.lapka.domain.models.Pet;
import ru.mirea.ivanovrr.lapka.domain.models.RecognitionResult;
import ru.mirea.ivanovrr.lapka.domain.models.Review;
import ru.mirea.ivanovrr.lapka.domain.models.Shelter;
import ru.mirea.ivanovrr.lapka.domain.models.User;
import ru.mirea.ivanovrr.lapka.domain.usecases.AddReviewUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetBreedInfoUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetEncountersUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetPetsByShelterUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetProfileUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetReviewsByShelterUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetSheltersByBreedUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.LogoutUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.RecognizeBreedUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.SaveEncounterUseCase;

/**
 * Временный экран-проверка: каждая кнопка вызывает use case, результат печатается в TextView.
 * Рядом с кнопкой подписан источник данных: NetworkApi, Room или SharedPreferences.
 * Все обращения к данным идут через фоновый поток, результат возвращается в UI-поток.
 */
public class MainActivity extends AppCompatActivity {

    private static final String DEMO_SHELTER_ID = "s1";
    private static final String DEMO_SHELTER_NAME = "Приют «Верный друг»";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    private TextView log;
    private TextView textViewUser;
    private Button buttonAccount;

    private GetProfileUseCase getProfileUseCase;
    private LogoutUseCase logoutUseCase;
    private RecognizeBreedUseCase recognizeBreedUseCase;
    private GetBreedInfoUseCase getBreedInfoUseCase;
    private GetSheltersByBreedUseCase getSheltersByBreedUseCase;
    private GetPetsByShelterUseCase getPetsByShelterUseCase;
    private SaveEncounterUseCase saveEncounterUseCase;
    private GetEncountersUseCase getEncountersUseCase;
    private AddReviewUseCase addReviewUseCase;
    private GetReviewsByShelterUseCase getReviewsByShelterUseCase;

    private String lastBreed = "Шпиц";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT));
        setContentView(R.layout.activity_main);
        InkHeader.applyInsets(findViewById(R.id.main), findViewById(R.id.header));

        log = findViewById(R.id.textViewLog);
        textViewUser = findViewById(R.id.textViewUser);
        buttonAccount = findViewById(R.id.buttonAccount);

        // зависимости берём из di — экран не создаёт репозитории сам
        ServiceLocator sl = ServiceLocator.getInstance(this);
        getProfileUseCase = sl.provideGetProfileUseCase();
        logoutUseCase = sl.provideLogoutUseCase();
        recognizeBreedUseCase = sl.provideRecognizeBreedUseCase();
        getBreedInfoUseCase = sl.provideGetBreedInfoUseCase();
        getSheltersByBreedUseCase = sl.provideGetSheltersByBreedUseCase();
        getPetsByShelterUseCase = sl.provideGetPetsByShelterUseCase();
        saveEncounterUseCase = sl.provideSaveEncounterUseCase();
        getEncountersUseCase = sl.provideGetEncountersUseCase();
        addReviewUseCase = sl.provideAddReviewUseCase();
        getReviewsByShelterUseCase = sl.provideGetReviewsByShelterUseCase();

        findViewById(R.id.buttonRecognize).setOnClickListener(v -> runInBackground(() -> {
            RecognitionResult result = recognizeBreedUseCase.execute("file://photo.jpg");
            if (result == null) {
                return "Не удалось распознать породу";
            }
            lastBreed = result.getBreedName();
            Breed breed = getBreedInfoUseCase.execute(lastBreed);
            String info = breed == null ? "" : "\n" + breed.getSpecies() + " · "
                    + breed.getTemperament() + "\n" + breed.getDescription();
            return String.format(Locale.getDefault(), "Порода: %s (%.0f%%)%s",
                    result.getBreedName(), result.getConfidence() * 100, info);
        }, this::print));

        findViewById(R.id.buttonShelters).setOnClickListener(v -> runInBackground(() -> {
            List<Shelter> shelters = getSheltersByBreedUseCase.execute(lastBreed);
            StringBuilder sb = new StringBuilder("Где есть порода " + lastBreed + ":");
            for (Shelter shelter : shelters) {
                sb.append("\n• ").append(shelter.getName())
                        .append(" — ").append(shelter.getAddress())
                        .append(", ").append(shelter.getWorkingHours());
            }
            return shelters.isEmpty() ? "Ничего не найдено" : sb.toString();
        }, this::print));

        findViewById(R.id.buttonPets).setOnClickListener(v -> runInBackground(() -> {
            List<Pet> pets = getPetsByShelterUseCase.execute(DEMO_SHELTER_ID);
            StringBuilder sb = new StringBuilder("Питомцы: " + DEMO_SHELTER_NAME);
            for (Pet pet : pets) {
                sb.append("\n• ").append(pet.getName())
                        .append(", ").append(pet.getBreedName())
                        .append(", ").append(pet.getAgeMonths()).append(" мес., ")
                        .append(pet.getGender());
            }
            return sb.toString();
        }, this::print));

        findViewById(R.id.buttonSaveEncounter).setOnClickListener(v -> runInBackground(() -> {
            long now = System.currentTimeMillis();
            Encounter encounter = new Encounter(UUID.randomUUID().toString(), lastBreed,
                    "file://photo.jpg", now, "встретил во дворе");
            if (!saveEncounterUseCase.execute(encounter)) {
                return "Гость не может сохранять встречи — сначала войдите";
            }
            return "Встреча сохранена в Room. Всего в альбоме: "
                    + getEncountersUseCase.execute().size();
        }, this::print));

        findViewById(R.id.buttonAlbum).setOnClickListener(v -> runInBackground(() -> {
            List<Encounter> encounters = getEncountersUseCase.execute();
            if (encounters.isEmpty()) {
                return "Альбом пуст (у гостя альбома нет)";
            }
            StringBuilder sb = new StringBuilder("Альбом встреч (" + encounters.size() + "):");
            for (Encounter e : encounters) {
                sb.append("\n• ").append(e.getBreedName())
                        .append(" — ").append(dateFormat.format(new Date(e.getDateMillis())))
                        .append(", ").append(e.getNote());
            }
            return sb.toString();
        }, this::print));

        findViewById(R.id.buttonAddReview).setOnClickListener(v -> runInBackground(() -> {
            User user = getProfileUseCase.execute();
            String author = user == null ? "Гость" : user.getName();
            Review review = new Review(UUID.randomUUID().toString(), DEMO_SHELTER_ID, author, 5,
                    "Отзывчивые волонтёры, животные ухоженные", System.currentTimeMillis());
            return addReviewUseCase.execute(review)
                    ? "Отзыв о «Верном друге» сохранён в Room"
                    : "Отзыв может оставить только авторизованный пользователь";
        }, this::print));

        findViewById(R.id.buttonReviews).setOnClickListener(v -> runInBackground(() -> {
            List<Review> reviews = getReviewsByShelterUseCase.execute(DEMO_SHELTER_ID);
            if (reviews.isEmpty()) {
                return "Отзывов пока нет";
            }
            StringBuilder sb = new StringBuilder("Отзывы: " + DEMO_SHELTER_NAME);
            for (Review r : reviews) {
                sb.append("\n• ").append(r.getAuthor())
                        .append(" — ").append(r.getRating()).append("/5: ")
                        .append(r.getText());
            }
            return sb.toString();
        }, this::print));

        findViewById(R.id.buttonProfile).setOnClickListener(v -> runInBackground(() -> {
            User user = getProfileUseCase.execute();
            if (user == null) {
                return "Вы в режиме гостя — профиля нет";
            }
            return "Профиль (из SharedPreferences):\nИмя: " + user.getName()
                    + "\nПочта: " + user.getEmail() + "\nUID: " + user.getId();
        }, this::print));

        buttonAccount.setOnClickListener(v -> {
            // у гостя кнопка ведёт на вход, у пользователя — выход из аккаунта
            logoutUseCase.execute();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        });

        showCurrentUser();
    }

    private void showCurrentUser() {
        User user = getProfileUseCase.execute();
        if (user == null) {
            textViewUser.setText(R.string.main_guest);
            buttonAccount.setText(R.string.main_login);
        } else {
            textViewUser.setText(getString(R.string.main_user, user.getName(), user.getEmail()));
            buttonAccount.setText(R.string.main_logout);
        }
    }

    /** Выполняет работу в фоне (Room и Firebase запрещены в UI-потоке) и печатает результат. */
    private <T> void runInBackground(Supplier<T> task, Consumer<T> onResult) {
        print("Загрузка…");
        executor.execute(() -> {
            T result = task.get();
            runOnUiThread(() -> onResult.accept(result));
        });
    }

    private void print(String text) {
        log.setText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
