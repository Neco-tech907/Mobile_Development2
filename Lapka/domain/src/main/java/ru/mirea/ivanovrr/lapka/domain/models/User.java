package ru.mirea.ivanovrr.lapka.domain.models;

/** Пользователь приложения. null вместо User означает «гость». */
public class User {
    private final String id;
    private final String email;
    private final String name;

    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
}
