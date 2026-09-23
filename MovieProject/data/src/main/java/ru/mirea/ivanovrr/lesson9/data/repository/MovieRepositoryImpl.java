package ru.mirea.ivanovrr.lesson9.data.repository;

import java.time.LocalDate;

import ru.mirea.ivanovrr.lesson9.data.storage.MovieStorage;
import ru.mirea.ivanovrr.lesson9.domain.models.Movie;
import ru.mirea.ivanovrr.lesson9.domain.repository.MovieRepository;

/**
 * Реализация репозитория из domain. Сам ничего не хранит — работает через
 * интерфейс MovieStorage и переводит модели: domain <-> storage.
 */
public class MovieRepositoryImpl implements MovieRepository {

    private final MovieStorage movieStorage;

    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        return movieStorage.save(mapToStorage(movie));
    }

    @Override
    public Movie getMovie() {
        ru.mirea.ivanovrr.lesson9.data.storage.models.Movie movie = movieStorage.get();
        return movie == null ? null : mapToDomain(movie);
    }

    private ru.mirea.ivanovrr.lesson9.data.storage.models.Movie mapToStorage(Movie movie) {
        return new ru.mirea.ivanovrr.lesson9.data.storage.models.Movie(
                movie.getId(), movie.getName(), LocalDate.now().toString());
    }

    private Movie mapToDomain(ru.mirea.ivanovrr.lesson9.data.storage.models.Movie movie) {
        return new Movie(movie.getId(), movie.getName());
    }
}
