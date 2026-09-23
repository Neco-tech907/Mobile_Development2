package ru.mirea.ivanovrr.lapka.data.storage.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.mirea.ivanovrr.lapka.data.storage.models.EncounterEntity;
import ru.mirea.ivanovrr.lapka.data.storage.models.ReviewEntity;

/** База данных приложения: альбом встреч и отзывы о приютах. */
@Database(entities = {EncounterEntity.class, ReviewEntity.class}, version = 1, exportSchema = false)
public abstract class LapkaDatabase extends RoomDatabase {

    private static final String DB_NAME = "lapka.db";
    private static volatile LapkaDatabase instance;

    public abstract EncounterDao encounterDao();

    public abstract ReviewDao reviewDao();

    /** Одна база на всё приложение — открывать её несколько раз дорого. */
    public static LapkaDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LapkaDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            LapkaDatabase.class, DB_NAME).build();
                }
            }
        }
        return instance;
    }
}
