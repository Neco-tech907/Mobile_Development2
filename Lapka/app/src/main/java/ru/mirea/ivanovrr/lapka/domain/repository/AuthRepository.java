package ru.mirea.ivanovrr.lapka.domain.repository;

import ru.mirea.ivanovrr.lapka.domain.models.User;

public interface AuthRepository {
    User login(String login, String password);
    User register(String login, String password, String name);
    User getCurrentUser();
    void logout();
}
