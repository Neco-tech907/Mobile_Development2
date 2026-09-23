package ru.mirea.ivanovrr.lapka.domain.repository;

import ru.mirea.ivanovrr.lapka.domain.models.AuthResult;
import ru.mirea.ivanovrr.lapka.domain.models.User;

/**
 * Контракт авторизации. Реализация (Firebase + SharedPreferences) живёт в модуле data,
 * domain про Firebase ничего не знает.
 * login и register — блокирующие, вызывать их нужно не из главного потока.
 */
public interface AuthRepository {
    AuthResult login(String email, String password);
    AuthResult register(String email, String password, String name);
    User getCurrentUser();
    void logout();
}
