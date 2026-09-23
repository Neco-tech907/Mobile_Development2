package ru.mirea.ivanovrr.lesson9.data.storage;

import ru.mirea.ivanovrr.lesson9.data.storage.models.Movie;

/**
 * Интерфейс хранилища. Репозиторий работает только с ним,
 * поэтому SharedPreferences можно заменить на Room или файл, не трогая репозиторий.
 */
public interface MovieStorage {

    Movie get();

    boolean save(Movie movie);
}
