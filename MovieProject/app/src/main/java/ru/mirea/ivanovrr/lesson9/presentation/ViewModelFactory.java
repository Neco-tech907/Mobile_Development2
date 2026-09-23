package ru.mirea.ivanovrr.lesson9.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.ivanovrr.lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.ivanovrr.lesson9.data.storage.MovieStorage;
import ru.mirea.ivanovrr.lesson9.data.storage.sharedprefs.SharedPrefMovieStorage;
import ru.mirea.ivanovrr.lesson9.domain.repository.MovieRepository;

/**
 * Фабрика создаёт MainViewModel вместе с зависимостями.
 * Context нужен только хранилищу, поэтому он остаётся здесь и не попадает во ViewModel.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public ViewModelFactory(Context context) {
        // applicationContext — фабрику не нужно привязывать к конкретной Activity
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            MovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(context);
            MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);
            return (T) new MainViewModel(movieRepository);
        }
        throw new IllegalArgumentException("Неизвестный класс ViewModel: " + modelClass.getName());
    }
}
