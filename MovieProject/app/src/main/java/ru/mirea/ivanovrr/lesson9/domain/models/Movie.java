package ru.mirea.ivanovrr.lesson9.domain.models;

/**
 * Сущность (Entity) слоя domain.
 * Не знает ни про Android, ни про базу, ни про сеть — это просто бизнес-объект.
 */
public class Movie {

    private final int id;
    private final String name;

    public Movie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
