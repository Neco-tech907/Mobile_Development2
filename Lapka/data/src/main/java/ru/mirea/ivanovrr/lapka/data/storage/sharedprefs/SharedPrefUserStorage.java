package ru.mirea.ivanovrr.lapka.data.storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.ivanovrr.lapka.data.storage.UserStorage;
import ru.mirea.ivanovrr.lapka.data.storage.models.UserStorageModel;

/** Хранит данные о клиенте в SharedPreferences (файл lapka_user). */
public class SharedPrefUserStorage implements UserStorage {

    private static final String PREFS_NAME = "lapka_user";
    private static final String KEY_UID = "uid";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "display_name";
    private static final String KEY_LAST_LOGIN = "last_login";

    private final SharedPreferences preferences;

    public SharedPrefUserStorage(Context context) {
        // applicationContext — чтобы не держать ссылку на Activity
        this.preferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean save(UserStorageModel user) {
        return preferences.edit()
                .putString(KEY_UID, user.getUid())
                .putString(KEY_EMAIL, user.getEmail())
                .putString(KEY_NAME, user.getDisplayName())
                .putLong(KEY_LAST_LOGIN, user.getLastLoginMillis())
                .commit();
    }

    @Override
    public UserStorageModel get() {
        String uid = preferences.getString(KEY_UID, null);
        if (uid == null) {
            return null;
        }
        return new UserStorageModel(
                uid,
                preferences.getString(KEY_EMAIL, ""),
                preferences.getString(KEY_NAME, ""),
                preferences.getLong(KEY_LAST_LOGIN, 0L));
    }

    @Override
    public void clear() {
        preferences.edit().clear().apply();
    }
}
