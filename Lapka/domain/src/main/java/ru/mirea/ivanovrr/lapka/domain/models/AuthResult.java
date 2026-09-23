package ru.mirea.ivanovrr.lapka.domain.models;

/**
 * Результат входа или регистрации: либо пользователь, либо текст ошибки.
 * Presentation показывает сообщение как есть и не знает, откуда оно пришло (Firebase или нет).
 */
public class AuthResult {
    private final User user;
    private final String errorMessage;

    private AuthResult(User user, String errorMessage) {
        this.user = user;
        this.errorMessage = errorMessage;
    }

    public static AuthResult success(User user) {
        return new AuthResult(user, null);
    }

    public static AuthResult failure(String errorMessage) {
        return new AuthResult(null, errorMessage);
    }

    public boolean isSuccess() { return user != null; }
    public User getUser() { return user; }
    public String getErrorMessage() { return errorMessage; }
}
