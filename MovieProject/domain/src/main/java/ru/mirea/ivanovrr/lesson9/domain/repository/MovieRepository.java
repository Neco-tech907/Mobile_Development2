package ru.mirea.ivanovrr.lesson9.domain.repository;

import ru.mirea.ivanovrr.lesson9.domain.models.Movie;

/**
 * Контракт доступа к данным. Объявлен в domain, а реализуется в data —
 * благодаря этому domain ни от кого не зависит (Dependency Rule).
 */
public interface MovieRepository {

    boolean saveMovie(Movie movie);

    Movie getMovie();
}
