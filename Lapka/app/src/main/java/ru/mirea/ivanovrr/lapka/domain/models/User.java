package ru.mirea.ivanovrr.lapka.domain.models;

/** Пользователь приложения. null вместо User означает «гость». */
public class User {
    private final String id;
    private final String login;
    private final String name;

    public User(String id, String login, String name) {
        this.id = id;
        this.login = login;
        this.name = name;
    }

    public String getId() { return id; }
    public String getLogin() { return login; }
    public String getName() { return name; }
}
