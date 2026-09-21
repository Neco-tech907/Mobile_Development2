package ru.mirea.ivanovrr.lesson9.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.ivanovrr.lesson9.domain.models.Movie;
import ru.mirea.ivanovrr.lesson9.domain.repository.MovieRepository;

/**
 * Реализация контракта из domain. Именно здесь — и только здесь — появляются
 * Context и SharedPreferences: слой data имеет право знать про Android,
 * а domain про него не знает и знать не должен.
 */
public class MovieRepositoryImpl implements MovieRepository {

    private static final String PREFS_NAME = "movie_prefs";
    private static final String KEY_MOVIE_ID = "movie_id";
    private static final String KEY_MOVIE_NAME = "movie_name";

    private final SharedPreferences preferences;

    public MovieRepositoryImpl(Context context) {
        // applicationContext — чтобы не утекла Activity
        this.preferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean saveMovie(Movie movie) {
        if (movie == null || movie.getName() == null || movie.getName().trim().isEmpty()) {
            return false;
        }
        preferences.edit()
                .putInt(KEY_MOVIE_ID, movie.getId())
                .putString(KEY_MOVIE_NAME, movie.getName())
                .apply();
        return true;
    }

    @Override
    public Movie getMovie() {
        String name = preferences.getString(KEY_MOVIE_NAME, null);
        if (name == null) {
            return null; // ничего ещё не сохраняли
        }
        return new Movie(preferences.getInt(KEY_MOVIE_ID, 0), name);
    }
}
