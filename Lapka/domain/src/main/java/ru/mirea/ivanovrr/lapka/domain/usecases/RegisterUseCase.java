package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.AuthResult;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;

public class RegisterUseCase {
    private final AuthRepository authRepository;

    public RegisterUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public AuthResult execute(String email, String password, String name) {
        if (name == null || name.trim().isEmpty()) {
            return AuthResult.failure("Для регистрации укажите имя");
        }
        if (email == null || !email.trim().contains("@")) {
            return AuthResult.failure("Введите корректную почту");
        }
        if (password == null || password.length() < LoginUseCase.MIN_PASSWORD_LENGTH) {
            return AuthResult.failure("Пароль — не короче "
                    + LoginUseCase.MIN_PASSWORD_LENGTH + " символов");
        }
        return authRepository.register(email.trim(), password, name.trim());
    }
}
