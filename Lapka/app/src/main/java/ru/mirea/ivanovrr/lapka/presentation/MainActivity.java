package ru.mirea.ivanovrr.lapka.presentation;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import ru.mirea.ivanovrr.lapka.R;
import ru.mirea.ivanovrr.lapka.data.repository.AuthRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.BreedRecognitionRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.BreedRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.EncounterRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.PetRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.ReviewRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.ShelterRepositoryImpl;
import ru.mirea.ivanovrr.lapka.domain.models.Breed;
import ru.mirea.ivanovrr.lapka.domain.models.Encounter;
import ru.mirea.ivanovrr.lapka.domain.models.Pet;
import ru.mirea.ivanovrr.lapka.domain.models.RecognitionResult;
import ru.mirea.ivanovrr.lapka.domain.models.Shelter;
import ru.mirea.ivanovrr.lapka.domain.models.User;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.BreedRecognitionRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.BreedRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.EncounterRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.PetRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.ReviewRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.ShelterRepository;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetBreedInfoUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetEncountersUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetPetsByShelterUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetSheltersByBreedUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.LoginUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.RecognizeBreedUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.SaveEncounterUseCase;

/**
 * Временный экран-проверка каркаса: кнопки дёргают use case'ы,
 * результат печатается в TextView. Настоящие экраны появятся в следующих работах.
 */
public class MainActivity extends AppCompatActivity {

    private TextView log;

    private RecognizeBreedUseCase recognizeBreedUseCase;
    private GetBreedInfoUseCase getBreedInfoUseCase;
    private GetSheltersByBreedUseCase getSheltersByBreedUseCase;
    private GetPetsByShelterUseCase getPetsByShelterUseCase;
    private LoginUseCase loginUseCase;
    private SaveEncounterUseCase saveEncounterUseCase;
    private GetEncountersUseCase getEncountersUseCase;

    private String lastBreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        log = findViewById(R.id.textViewLog);

        // Слой presentation собирает зависимости: реализации из data
        // подставляются в use case'ы через конструктор
        AuthRepository authRepository = new AuthRepositoryImpl();
        BreedRecognitionRepository recognitionRepository = new BreedRecognitionRepositoryImpl();
        BreedRepository breedRepository = new BreedRepositoryImpl();
        ShelterRepository shelterRepository = new ShelterRepositoryImpl();
        PetRepository petRepository = new PetRepositoryImpl();
        EncounterRepository encounterRepository = new EncounterRepositoryImpl();
        ReviewRepository reviewRepository = new ReviewRepositoryImpl();

        recognizeBreedUseCase = new RecognizeBreedUseCase(recognitionRepository);
        getBreedInfoUseCase = new GetBreedInfoUseCase(breedRepository);
        getSheltersByBreedUseCase = new GetSheltersByBreedUseCase(shelterRepository);
        getPetsByShelterUseCase = new GetPetsByShelterUseCase(petRepository);
        loginUseCase = new LoginUseCase(authRepository);
        saveEncounterUseCase = new SaveEncounterUseCase(encounterRepository, authRepository);
        getEncountersUseCase = new GetEncountersUseCase(encounterRepository, authRepository);

        findViewById(R.id.buttonRecognize).setOnClickListener(v -> {
            RecognitionResult result = recognizeBreedUseCase.execute("file://photo.jpg");
            if (result == null) {
                print("Не удалось распознать породу");
                return;
            }
            lastBreed = result.getBreedName();
            Breed breed = getBreedInfoUseCase.execute(lastBreed);
            String info = breed == null ? "" : "\n" + breed.getTemperament();
            print(String.format("Порода: %s (%.0f%%)%s",
                    result.getBreedName(), result.getConfidence() * 100, info));
        });

        findViewById(R.id.buttonShelters).setOnClickListener(v -> {
            String breed = lastBreed == null ? "Шпиц" : lastBreed;
            List<Shelter> shelters = getSheltersByBreedUseCase.execute(breed);
            StringBuilder sb = new StringBuilder("Где есть порода " + breed + ":");
            for (Shelter shelter : shelters) {
                sb.append("\n• ").append(shelter.getName())
                        .append(" — ").append(shelter.getAddress());
            }
            print(sb.toString());
        });

        findViewById(R.id.buttonPets).setOnClickListener(v -> {
            List<Pet> pets = getPetsByShelterUseCase.execute("s1");
            StringBuilder sb = new StringBuilder("Питомцы приюта «Верный друг»:");
            for (Pet pet : pets) {
                sb.append("\n• ").append(pet.getName())
                        .append(", ").append(pet.getBreedName())
                        .append(", ").append(pet.getAgeMonths()).append(" мес., ")
                        .append(pet.getGender());
            }
            print(sb.toString());
        });

        findViewById(R.id.buttonLogin).setOnClickListener(v -> {
            User user = loginUseCase.execute("user", "1234");
            print(user == null ? "Вход не выполнен" : "Вошёл как: " + user.getName());
        });

        findViewById(R.id.buttonSaveEncounter).setOnClickListener(v -> {
            String breed = lastBreed == null ? "Шпиц" : lastBreed;
            Encounter encounter = new Encounter(
                    String.valueOf(System.currentTimeMillis()), breed,
                    "file://photo.jpg", System.currentTimeMillis(), "встретил во дворе");
            boolean saved = saveEncounterUseCase.execute(encounter);
            int count = getEncountersUseCase.execute().size();
            print(saved
                    ? "Встреча сохранена. Всего в альбоме: " + count
                    : "Гость не может сохранять встречи — сначала войди");
        });
    }

    private void print(String text) {
        log.setText(text);
    }
}
