package ru.mirea.ivanovrr.lapka.data.firebase;

/**
 * Пользователь, как его вернул Firebase. Нужен, чтобы тип FirebaseUser
 * не выходил за пределы пакета firebase.
 */
public class AuthUserDto {
    public final String uid;
    public final String email;
    public final String displayName;

    public AuthUserDto(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
    }
}
