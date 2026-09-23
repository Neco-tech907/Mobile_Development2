package ru.mirea.ivanovrr.lesson9.data.storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.ivanovrr.lesson9.data.storage.MovieStorage;
import ru.mirea.ivanovrr.lesson9.data.storage.models.Movie;

/** Реализация хранилища на SharedPreferences. Только здесь есть Context. */
public class SharedPrefMovieStorage implements MovieStorage {

    private static final String SHARED_PREFS_NAME = "shared_prefs_name";
    private static final String KEY_NAME = "movie_name";
    private static final String KEY_DATE = "movie_date";
    private static final String KEY_ID = "movie_id";

    private final SharedPreferences sharedPreferences;

    public SharedPrefMovieStorage(Context context) {
        // applicationContext — чтобы не держать ссылку на Activity
        this.sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public Movie get() {
        String name = sharedPreferences.getString(KEY_NAME, null);
        if (name == null) {
            return null; // ещё ничего не сохраняли
        }
        String date = sharedPreferences.getString(KEY_DATE, "");
        int id = sharedPreferences.getInt(KEY_ID, -1);
        return new Movie(id, name, date);
    }

    @Override
    public boolean save(Movie movie) {
        return sharedPreferences.edit()
                .putString(KEY_NAME, movie.getName())
                .putString(KEY_DATE, movie.getLocalDate())
                .putInt(KEY_ID, movie.getId())
                .commit();
    }
}
