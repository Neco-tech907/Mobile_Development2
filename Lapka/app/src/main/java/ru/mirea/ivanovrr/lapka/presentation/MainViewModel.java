package ru.mirea.ivanovrr.lapka.presentation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import ru.mirea.ivanovrr.lapka.domain.models.Breed;
import ru.mirea.ivanovrr.lapka.domain.models.DataException;
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
import ru.mirea.ivanovrr.lapka.domain.usecases.GetShelterDetailsUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetSheltersByBreedUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.LogoutUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.RecognizeBreedUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.SaveEncounterUseCase;

/**
 * ViewModel главного экрана. Вся работа с use case'ами перенесена сюда из MainActivity.
 * Состояние экрана лежит в LiveData и переживает поворот:
 * log — результат последней кнопки, currentUser — кто вошёл,
 * shelterDetails — MediatorLiveData, собранная из сети (приют) и Room (отзывы).
 */
public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    static final String DEMO_SHELTER_ID = "s1";
    private static final String DEMO_SHELTER_NAME = "Приют «Верный друг»";

    private final GetProfileUseCase getProfileUseCase;
    private final LogoutUseCase logoutUseCase;
    private final RecognizeBreedUseCase recognizeBreedUseCase;
    private final GetBreedInfoUseCase getBreedInfoUseCase;
    private final GetSheltersByBreedUseCase getSheltersByBreedUseCase;
    private final GetShelterDetailsUseCase getShelterDetailsUseCase;
    private final GetPetsByShelterUseCase getPetsByShelterUseCase;
    private final SaveEncounterUseCase saveEncounterUseCase;
    private final GetEncountersUseCase getEncountersUseCase;
    private final AddReviewUseCase addReviewUseCase;
    private final GetReviewsByShelterUseCase getReviewsByShelterUseCase;

    // Два потока: сеть и Room могут загружаться одновременно
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    private final MutableLiveData<String> log = new MutableLiveData<>();
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loggedOut = new MutableLiveData<>(false);
    // Список приютов для RecyclerView — заглушка NetworkApi, отданная экрану через LiveData
    private final MutableLiveData<List<Shelter>> shelters = new MutableLiveData<>();
    private final MutableLiveData<List<Pet>> pets = new MutableLiveData<>();

    // --- MediatorLiveData: два источника, один результат ---
    private final MutableLiveData<Shelter> shelterFromNetwork = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> reviewsFromDb = new MutableLiveData<>();
    private final MediatorLiveData<String> shelterDetails = new MediatorLiveData<>();

    private volatile String lastBreed = "Шпиц";

    public MainViewModel(GetProfileUseCase getProfileUseCase,
                         LogoutUseCase logoutUseCase,
                         RecognizeBreedUseCase recognizeBreedUseCase,
                         GetBreedInfoUseCase getBreedInfoUseCase,
                         GetSheltersByBreedUseCase getSheltersByBreedUseCase,
                         GetShelterDetailsUseCase getShelterDetailsUseCase,
                         GetPetsByShelterUseCase getPetsByShelterUseCase,
                         SaveEncounterUseCase saveEncounterUseCase,
                         GetEncountersUseCase getEncountersUseCase,
                         AddReviewUseCase addReviewUseCase,
                         GetReviewsByShelterUseCase getReviewsByShelterUseCase) {
        Log.d(TAG, "MainViewModel created");
        this.getProfileUseCase = getProfileUseCase;
        this.logoutUseCase = logoutUseCase;
        this.recognizeBreedUseCase = recognizeBreedUseCase;
        this.getBreedInfoUseCase = getBreedInfoUseCase;
        this.getSheltersByBreedUseCase = getSheltersByBreedUseCase;
        this.getShelterDetailsUseCase = getShelterDetailsUseCase;
        this.getPetsByShelterUseCase = getPetsByShelterUseCase;
        this.saveEncounterUseCase = saveEncounterUseCase;
        this.getEncountersUseCase = getEncountersUseCase;
        this.addReviewUseCase = addReviewUseCase;
        this.getReviewsByShelterUseCase = getReviewsByShelterUseCase;

        // Mediator слушает оба источника и пересобирает карточку, когда меняется любой из них
        shelterDetails.addSource(shelterFromNetwork, shelter -> combineShelterDetails());
        shelterDetails.addSource(reviewsFromDb, reviews -> combineShelterDetails());

        currentUser.setValue(getProfileUseCase.execute());
    }

    public LiveData<String> getLog() { return log; }
    public LiveData<User> getCurrentUser() { return currentUser; }
    public LiveData<Boolean> getLoggedOut() { return loggedOut; }
    public LiveData<String> getShelterDetails() { return shelterDetails; }
    public LiveData<List<Shelter>> getShelters() { return shelters; }
    public LiveData<List<Pet>> getPets() { return pets; }

    // ---------- каталог: NetworkApi ----------

    public void recognize() {
        runInBackground(() -> {
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
        });
    }

    /** Загружает приюты с породой lastBreed и отдаёт их списку через LiveData. */
    public void loadShelters() {
        runInBackground(() -> {
            List<Shelter> found = getSheltersByBreedUseCase.execute(lastBreed);
            shelters.postValue(found);
            return found.isEmpty()
                    ? "Ничего не найдено"
                    : "Где есть порода " + lastBreed + ": " + found.size() + " мест — список ниже";
        });
    }

    /** Загружает питомцев приюта из сети и отдаёт их списку с фото через LiveData. */
    public void loadPets() {
        runInBackground(() -> {
            List<Pet> found = getPetsByShelterUseCase.execute(DEMO_SHELTER_ID);
            pets.postValue(found);
            return "Питомцы " + DEMO_SHELTER_NAME + ": " + found.size() + " — список ниже";
        });
    }

    // ---------- альбом и отзывы: Room ----------

    public void saveEncounter() {
        runInBackground(() -> {
            long now = System.currentTimeMillis();
            Encounter encounter = new Encounter(UUID.randomUUID().toString(), lastBreed,
                    "file://photo.jpg", now, "встретил во дворе");
            if (!saveEncounterUseCase.execute(encounter)) {
                return "Гость не может сохранять встречи — сначала войдите";
            }
            return "Встреча сохранена в Room. Всего в альбоме: "
                    + getEncountersUseCase.execute().size();
        });
    }

    public void loadAlbum() {
        runInBackground(() -> {
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
        });
    }

    public void addReview() {
        runInBackground(() -> {
            User user = getProfileUseCase.execute();
            String author = user == null ? "Гость" : user.getName();
            Review review = new Review(UUID.randomUUID().toString(), DEMO_SHELTER_ID, author, 5,
                    "Отзывчивые волонтёры, животные ухоженные", System.currentTimeMillis());
            if (!addReviewUseCase.execute(review)) {
                return "Отзыв может оставить только авторизованный пользователь";
            }
            // Room изменился — обновляем источник, и MediatorLiveData сама пересоберёт карточку
            reviewsFromDb.postValue(getReviewsByShelterUseCase.execute(DEMO_SHELTER_ID));
            return "Отзыв о «Верном друге» сохранён в Room";
        });
    }

    public void loadReviews() {
        runInBackground(() -> {
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
        });
    }

    // ---------- MediatorLiveData: приют из сети + отзывы из Room ----------

    /**
     * Запускает обе загрузки одновременно. Отзывы из Room приходят почти сразу,
     * приют из NetworkApi — через ~400 мс, и карточка обновляется на каждый ответ.
     */
    public void loadShelterDetails() {
        shelterFromNetwork.setValue(null);
        reviewsFromDb.setValue(null);
        executor.execute(() -> {
            try {
                shelterFromNetwork.postValue(getShelterDetailsUseCase.execute(DEMO_SHELTER_ID));
            } catch (DataException e) {
                // сеть недоступна — карточка покажет ошибку вместо приюта
                shelterDetails.postValue("Ошибка сети: " + e.getMessage());
            }
        });
        executor.execute(() ->
                reviewsFromDb.postValue(getReviewsByShelterUseCase.execute(DEMO_SHELTER_ID)));
    }

    private void combineShelterDetails() {
        Shelter shelter = shelterFromNetwork.getValue();
        List<Review> reviews = reviewsFromDb.getValue();

        StringBuilder sb = new StringBuilder();
        if (shelter == null) {
            sb.append("Приют: загрузка из сети…");
        } else {
            sb.append(shelter.getName()).append(" · ").append(shelter.getType())
                    .append("\n").append(shelter.getAddress())
                    .append("\n").append(shelter.getPhone())
                    .append(", ").append(shelter.getWorkingHours())
                    .append("\n(из NetworkApi)");
        }
        sb.append("\n\n");
        if (reviews == null) {
            sb.append("Отзывы: загрузка из базы…");
        } else if (reviews.isEmpty()) {
            sb.append("Отзывов пока нет (из Room)");
        } else {
            float sum = 0;
            for (Review r : reviews) {
                sum += r.getRating();
            }
            sb.append(String.format(Locale.getDefault(), "Рейтинг %.1f · отзывов: %d (из Room)",
                    sum / reviews.size(), reviews.size()));
            for (Review r : reviews) {
                sb.append("\n• ").append(r.getAuthor()).append(": ").append(r.getText());
            }
        }
        shelterDetails.setValue(sb.toString());
    }

    // ---------- профиль ----------

    public void loadProfile() {
        runInBackground(() -> {
            User user = getProfileUseCase.execute();
            currentUser.postValue(user);
            if (user == null) {
                return "Вы в режиме гостя — профиля нет";
            }
            return "Профиль (из SharedPreferences):\nИмя: " + user.getName()
                    + "\nПочта: " + user.getEmail() + "\nUID: " + user.getId();
        });
    }

    /** У гостя кнопка ведёт на вход, у пользователя — выход из аккаунта. */
    public void logout() {
        logoutUseCase.execute();
        currentUser.setValue(null);
        loggedOut.setValue(true);
    }

    /** Показывает «Загрузка…», выполняет работу в фоне и кладёт ответ в LiveData. */
    private void runInBackground(Supplier<String> task) {
        log.setValue("Загрузка…");
        executor.execute(() -> {
            try {
                log.postValue(task.get());
            } catch (DataException e) {
                // Retrofit не достучался до сервера или ответ битый — показываем причину
                log.postValue("Ошибка: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "MainViewModel cleared");
        executor.shutdownNow();
        super.onCleared();
    }
}
