package ru.mirea.ivanovrr.lapka.data.firebase;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Обёртка над Firebase Authentication (вход по почте и паролю).
 * Единственное место в проекте, где есть классы Firebase.
 * Асинхронные Task'и Firebase здесь превращаются в обычные блокирующие вызовы
 * через Tasks.await, поэтому методы signIn/signUp нельзя вызывать из главного потока.
 */
public class FirebaseAuthSource {

    private static final long TIMEOUT_SECONDS = 15;

    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthSource() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public AuthUserDto signIn(String email, String password) throws AuthSourceException {
        AuthResult result = await(firebaseAuth.signInWithEmailAndPassword(email, password));
        return mapUser(result.getUser());
    }

    public AuthUserDto signUp(String email, String password, String name)
            throws AuthSourceException {
        AuthResult result = await(firebaseAuth.createUserWithEmailAndPassword(email, password));
        FirebaseUser user = result.getUser();
        if (user == null) {
            throw new AuthSourceException("Не удалось создать пользователя", null);
        }
        // имя храним прямо в профиле Firebase, чтобы оно было доступно на любом устройстве
        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        await(user.updateProfile(profile));
        return new AuthUserDto(user.getUid(), user.getEmail(), name);
    }

    /** Firebase сам помнит вошедшего пользователя между запусками приложения. */
    public AuthUserDto getCurrentUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user == null ? null : mapUser(user);
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    private AuthUserDto mapUser(FirebaseUser user) {
        if (user == null) {
            return null;
        }
        return new AuthUserDto(user.getUid(), user.getEmail(), user.getDisplayName());
    }

    private <T> T await(Task<T> task) throws AuthSourceException {
        try {
            return Tasks.await(task, TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            throw new AuthSourceException(mapError(e.getCause()), e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AuthSourceException("Операция прервана", e);
        } catch (TimeoutException e) {
            throw new AuthSourceException("Сервер не отвечает, попробуйте ещё раз", e);
        }
    }

    /** Переводит исключения Firebase в понятные пользователю сообщения. */
    private String mapError(Throwable error) {
        // WeakPassword наследуется от InvalidCredentials, поэтому проверяется раньше
        if (error instanceof FirebaseAuthWeakPasswordException) {
            return "Слишком простой пароль — нужно не меньше 6 символов";
        }
        if (error instanceof FirebaseAuthUserCollisionException) {
            return "Пользователь с такой почтой уже зарегистрирован";
        }
        if (error instanceof FirebaseAuthInvalidUserException) {
            return "Пользователь не найден или заблокирован";
        }
        if (error instanceof FirebaseAuthInvalidCredentialsException) {
            return "Неверная почта или пароль";
        }
        if (error instanceof FirebaseNetworkException) {
            return "Нет подключения к интернету";
        }
        if (error instanceof FirebaseTooManyRequestsException) {
            return "Слишком много попыток, попробуйте позже";
        }
        return "Ошибка авторизации: " + (error == null ? "неизвестная" : error.getMessage());
    }
}
