package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.AuthResult;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;

/** Проверяет ввод до обращения к серверу, чтобы не гонять заведомо неверный запрос. */
public class LoginUseCase {
    static final int MIN_PASSWORD_LENGTH = 6;

    private final AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public AuthResult execute(String email, String password) {
        if (email == null || !email.trim().contains("@")) {
            return AuthResult.failure("Введите корректную почту");
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return AuthResult.failure("Пароль — не короче " + MIN_PASSWORD_LENGTH + " символов");
        }
        return authRepository.login(email.trim(), password);
    }
}
