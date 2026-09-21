package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.User;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;

public class LoginUseCase {
    private final AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public User execute(String login, String password) {
        if (login == null || login.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        return authRepository.login(login, password);
    }
}
