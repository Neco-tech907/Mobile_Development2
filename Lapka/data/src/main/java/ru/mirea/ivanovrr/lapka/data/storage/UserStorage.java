package ru.mirea.ivanovrr.lapka.data.storage;

import ru.mirea.ivanovrr.lapka.data.storage.models.UserStorageModel;

/** Хранилище информации о клиенте. Реализация — SharedPrefUserStorage. */
public interface UserStorage {
    boolean save(UserStorageModel user);
    UserStorageModel get();
    void clear();
}
