package ru.mirea.ivanovrr.lesson9.domain.usecases;

import ru.mirea.ivanovrr.lesson9.domain.models.Movie;
import ru.mirea.ivanovrr.lesson9.domain.repository.MovieRepository;

/**
 * Сценарий использования «получить любимый фильм».
 * Репозиторий приходит через конструктор (Dependency Injection),
 * поэтому use case не знает, откуда именно берутся данные.
 */
public class GetFavoriteFilmUseCase {

    private final MovieRepository movieRepository;

    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie execute() {
        return movieRepository.getMovie();
    }
}
