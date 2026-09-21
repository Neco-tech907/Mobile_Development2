package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.User;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;

/** Возвращает текущего пользователя или null, если это гость. */
public class GetProfileUseCase {
    private final AuthRepository authRepository;

    public GetProfileUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public User execute() {
        return authRepository.getCurrentUser();
    }
}
