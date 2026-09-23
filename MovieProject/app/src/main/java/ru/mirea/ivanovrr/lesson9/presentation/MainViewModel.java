package ru.mirea.ivanovrr.lesson9.presentation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.ivanovrr.lesson9.domain.models.Movie;
import ru.mirea.ivanovrr.lesson9.domain.repository.MovieRepository;
import ru.mirea.ivanovrr.lesson9.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.ivanovrr.lesson9.domain.usecases.SaveMovieToFavoriteUseCase;

/**
 * ViewModel экрана MainActivity. Здесь теперь живёт вся работа с use case'ами,
 * а Activity только показывает то, что лежит в LiveData.
 * Про Activity и Context ViewModel ничего не знает: репозиторий приходит через конструктор.
 */
public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private final MovieRepository movieRepository;

    // Текст для textViewMovie. Переживает поворот экрана вместе с ViewModel
    private final MutableLiveData<String> favoriteMovie = new MutableLiveData<>();

    public MainViewModel(MovieRepository movieRepository) {
        Log.d(TAG, "MainViewModel created");
        this.movieRepository = movieRepository;
    }

    public LiveData<String> getFavoriteMovie() {
        return favoriteMovie;
    }

    /** Сохранить фильм. Результат (true / false) уходит в LiveData, а не возвращается. */
    public void setText(Movie movie) {
        boolean result = new SaveMovieToFavoriteUseCase(movieRepository).execute(movie);
        favoriteMovie.setValue(String.format("Save result %s", result));
    }

    /** Прочитать сохранённый фильм и положить готовый текст в LiveData. */
    public void getText() {
        Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
        if (movie == null) {
            favoriteMovie.setValue("Нет данных!");
        } else {
            favoriteMovie.setValue(String.format("Любимый фильм: %s", movie.getName()));
        }
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "MainViewModel cleared");
        super.onCleared();
    }
}
