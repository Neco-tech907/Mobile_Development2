package ru.mirea.ivanovrr.lapka.data.repository;

import ru.mirea.ivanovrr.lapka.data.firebase.AuthSourceException;
import ru.mirea.ivanovrr.lapka.data.firebase.AuthUserDto;
import ru.mirea.ivanovrr.lapka.data.firebase.FirebaseAuthSource;
import ru.mirea.ivanovrr.lapka.data.storage.UserStorage;
import ru.mirea.ivanovrr.lapka.data.storage.models.UserStorageModel;
import ru.mirea.ivanovrr.lapka.domain.models.AuthResult;
import ru.mirea.ivanovrr.lapka.domain.models.User;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;

/**
 * Авторизация: сам вход выполняет Firebase, а данные о клиенте (uid, почта, имя,
 * время последнего входа) кладутся в SharedPreferences через UserStorage.
 */
public class AuthRepositoryImpl implements AuthRepository {

    private final FirebaseAuthSource authSource;
    private final UserStorage userStorage;

    public AuthRepositoryImpl(FirebaseAuthSource authSource, UserStorage userStorage) {
        this.authSource = authSource;
        this.userStorage = userStorage;
    }

    @Override
    public AuthResult login(String email, String password) {
        try {
            AuthUserDto user = authSource.signIn(email, password);
            UserStorageModel stored = mapToStorage(user);
            userStorage.save(stored);
            return AuthResult.success(mapToDomain(stored));
        } catch (AuthSourceException e) {
            return AuthResult.failure(e.getMessage());
        }
    }

    @Override
    public AuthResult register(String email, String password, String name) {
        try {
            AuthUserDto user = authSource.signUp(email, password, name);
            UserStorageModel stored = mapToStorage(user);
            userStorage.save(stored);
            return AuthResult.success(mapToDomain(stored));
        } catch (AuthSourceException e) {
            return AuthResult.failure(e.getMessage());
        }
    }

    @Override
    public User getCurrentUser() {
        AuthUserDto firebaseUser = authSource.getCurrentUser();
        if (firebaseUser == null) {
            userStorage.clear();
            return null;
        }
        UserStorageModel stored = userStorage.get();
        if (stored == null || !stored.getUid().equals(firebaseUser.uid)) {
            // сессия в Firebase есть, а локальной записи нет — восстанавливаем её
            stored = mapToStorage(firebaseUser);
            userStorage.save(stored);
        }
        return mapToDomain(stored);
    }

    @Override
    public void logout() {
        authSource.signOut();
        userStorage.clear();
    }

    private UserStorageModel mapToStorage(AuthUserDto user) {
        String name = user.displayName;
        if (name == null || name.trim().isEmpty()) {
            // у пользователей, созданных в консоли Firebase, имени нет — берём начало почты
            String email = user.email == null ? "" : user.email;
            int at = email.indexOf('@');
            name = at > 0 ? email.substring(0, at) : email;
        }
        return new UserStorageModel(user.uid, user.email, name, System.currentTimeMillis());
    }

    private User mapToDomain(UserStorageModel stored) {
        return new User(stored.getUid(), stored.getEmail(), stored.getDisplayName());
    }
}
