package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.User;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;

public class RegisterUseCase {
    private final AuthRepository authRepository;

    public RegisterUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public User execute(String login, String password, String name) {
        if (login == null || login.trim().isEmpty() || password == null || password.length() < 4) {
            return null;
        }
        return authRepository.register(login, password, name);
    }
}
