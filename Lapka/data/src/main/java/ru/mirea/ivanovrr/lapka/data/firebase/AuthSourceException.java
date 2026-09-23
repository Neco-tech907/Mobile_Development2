package ru.mirea.ivanovrr.lapka.data.firebase;

/** Ошибка Firebase Auth, уже переведённая в понятное пользователю сообщение. */
public class AuthSourceException extends Exception {
    public AuthSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
