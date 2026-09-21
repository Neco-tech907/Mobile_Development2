package ru.mirea.ivanovrr.lapka.data.repository;

import ru.mirea.ivanovrr.lapka.domain.models.User;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;

/**
 * Заглушка авторизации. Позже здесь появятся SharedPreferences / Room,
 * а domain об этом даже не узнает.
 */
public class AuthRepositoryImpl implements AuthRepository {

    private static final String TEST_LOGIN = "user";
    private static final String TEST_PASSWORD = "1234";

    private User currentUser;

    @Override
    public User login(String login, String password) {
        if (TEST_LOGIN.equals(login) && TEST_PASSWORD.equals(password)) {
            currentUser = new User("u1", login, "Тестовый пользователь");
            return currentUser;
        }
        return null;
    }

    @Override
    public User register(String login, String password, String name) {
        currentUser = new User("u2", login, name);
        return currentUser;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void logout() {
        currentUser = null;
    }
}
