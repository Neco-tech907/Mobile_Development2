package ru.mirea.ivanovrr.lapka.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.ivanovrr.lapka.di.ServiceLocator;

/**
 * Фабрика ViewModel'ей. Берёт готовые use case'ы из ServiceLocator и передаёт их в конструктор,
 * поэтому ViewModel не знает ни про Context, ни про Firebase, Room и SharedPreferences.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ServiceLocator serviceLocator;

    public ViewModelFactory(Context context) {
        this.serviceLocator = ServiceLocator.getInstance(context);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        ServiceLocator sl = serviceLocator;
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(
                    sl.provideLoginUseCase(),
                    sl.provideRegisterUseCase(),
                    sl.provideGetProfileUseCase());
        }
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(
                    sl.provideGetProfileUseCase(),
                    sl.provideLogoutUseCase(),
                    sl.provideRecognizeBreedUseCase(),
                    sl.provideGetBreedInfoUseCase(),
                    sl.provideGetSheltersByBreedUseCase(),
                    sl.provideGetShelterDetailsUseCase(),
                    sl.provideGetPetsByShelterUseCase(),
                    sl.provideSaveEncounterUseCase(),
                    sl.provideGetEncountersUseCase(),
                    sl.provideAddReviewUseCase(),
                    sl.provideGetReviewsByShelterUseCase());
        }
        throw new IllegalArgumentException("Неизвестный класс ViewModel: " + modelClass.getName());
    }
}
