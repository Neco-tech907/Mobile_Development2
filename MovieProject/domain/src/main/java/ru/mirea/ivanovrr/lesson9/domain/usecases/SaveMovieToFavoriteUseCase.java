package ru.mirea.ivanovrr.lesson9.domain.usecases;

import ru.mirea.ivanovrr.lesson9.domain.models.Movie;
import ru.mirea.ivanovrr.lesson9.domain.repository.MovieRepository;

/**
 * Сценарий использования «сохранить фильм в любимые».
 * Здесь же лежит бизнес-правило: пустое название сохранять нельзя.
 */
public class SaveMovieToFavoriteUseCase {

    private final MovieRepository movieRepository;

    public SaveMovieToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public boolean execute(Movie movie) {
        if (movie == null || movie.getName() == null || movie.getName().trim().isEmpty()) {
            return false;
        }
        return movieRepository.saveMovie(movie);
    }
}
