package ru.mirea.ivanovrr.lapka.di;

import android.content.Context;

import ru.mirea.ivanovrr.lapka.data.firebase.FirebaseAuthSource;
import ru.mirea.ivanovrr.lapka.data.network.NetworkApi;
import ru.mirea.ivanovrr.lapka.data.repository.AuthRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.BreedRecognitionRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.BreedRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.EncounterRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.PetRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.ReviewRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.repository.ShelterRepositoryImpl;
import ru.mirea.ivanovrr.lapka.data.storage.room.RoomEncounterStorage;
import ru.mirea.ivanovrr.lapka.data.storage.room.RoomReviewStorage;
import ru.mirea.ivanovrr.lapka.data.storage.sharedprefs.SharedPrefUserStorage;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.BreedRecognitionRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.BreedRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.EncounterRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.PetRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.ReviewRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.ShelterRepository;
import ru.mirea.ivanovrr.lapka.domain.usecases.AddReviewUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetBreedInfoUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetEncountersUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetPetsByShelterUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetProfileUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetReviewsByShelterUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetShelterDetailsUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetSheltersByBreedUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.LoginUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.LogoutUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.RecognizeBreedUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.RegisterUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.SaveEncounterUseCase;

/**
 * Ручной DI: единственное место, где presentation встречается с классами из data.
 * Репозитории создаются один раз на всё приложение, use case'ы — по запросу.
 * Экраны получают только use case'ы и ничего не знают о Firebase, Room и SharedPreferences.
 */
public final class ServiceLocator {

    private static volatile ServiceLocator instance;

    private final AuthRepository authRepository;
    private final BreedRecognitionRepository recognitionRepository;
    private final BreedRepository breedRepository;
    private final ShelterRepository shelterRepository;
    private final PetRepository petRepository;
    private final EncounterRepository encounterRepository;
    private final ReviewRepository reviewRepository;

    private ServiceLocator(Context appContext) {
        NetworkApi networkApi = new NetworkApi();

        // Firebase + SharedPreferences
        authRepository = new AuthRepositoryImpl(new FirebaseAuthSource(),
                new SharedPrefUserStorage(appContext));
        recognitionRepository = new BreedRecognitionRepositoryImpl();
        // NetworkApi с замоканными ответами
        breedRepository = new BreedRepositoryImpl(networkApi);
        shelterRepository = new ShelterRepositoryImpl(networkApi);
        petRepository = new PetRepositoryImpl(networkApi);
        // Room
        encounterRepository = new EncounterRepositoryImpl(new RoomEncounterStorage(appContext));
        reviewRepository = new ReviewRepositoryImpl(new RoomReviewStorage(appContext));
    }

    public static ServiceLocator getInstance(Context context) {
        if (instance == null) {
            synchronized (ServiceLocator.class) {
                if (instance == null) {
                    instance = new ServiceLocator(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    // --- авторизация ---
    public LoginUseCase provideLoginUseCase() {
        return new LoginUseCase(authRepository);
    }

    public RegisterUseCase provideRegisterUseCase() {
        return new RegisterUseCase(authRepository);
    }

    public GetProfileUseCase provideGetProfileUseCase() {
        return new GetProfileUseCase(authRepository);
    }

    public LogoutUseCase provideLogoutUseCase() {
        return new LogoutUseCase(authRepository);
    }

    // --- каталог ---
    public RecognizeBreedUseCase provideRecognizeBreedUseCase() {
        return new RecognizeBreedUseCase(recognitionRepository);
    }

    public GetBreedInfoUseCase provideGetBreedInfoUseCase() {
        return new GetBreedInfoUseCase(breedRepository);
    }

    public GetSheltersByBreedUseCase provideGetSheltersByBreedUseCase() {
        return new GetSheltersByBreedUseCase(shelterRepository);
    }

    public GetShelterDetailsUseCase provideGetShelterDetailsUseCase() {
        return new GetShelterDetailsUseCase(shelterRepository);
    }

    public GetPetsByShelterUseCase provideGetPetsByShelterUseCase() {
        return new GetPetsByShelterUseCase(petRepository);
    }

    // --- альбом и отзывы ---
    public SaveEncounterUseCase provideSaveEncounterUseCase() {
        return new SaveEncounterUseCase(encounterRepository, authRepository);
    }

    public GetEncountersUseCase provideGetEncountersUseCase() {
        return new GetEncountersUseCase(encounterRepository, authRepository);
    }

    public AddReviewUseCase provideAddReviewUseCase() {
        return new AddReviewUseCase(reviewRepository, authRepository);
    }

    public GetReviewsByShelterUseCase provideGetReviewsByShelterUseCase() {
        return new GetReviewsByShelterUseCase(reviewRepository);
    }
}
