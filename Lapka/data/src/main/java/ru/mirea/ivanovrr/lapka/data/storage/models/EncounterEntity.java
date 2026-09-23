package ru.mirea.ivanovrr.lapka.data.storage.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Строка таблицы encounters (альбом встреч) в Room. */
@Entity(tableName = "encounters")
public class EncounterEntity {

    @PrimaryKey
    @NonNull
    public String id = "";

    @ColumnInfo(name = "breed_name")
    public String breedName;

    @ColumnInfo(name = "photo_uri")
    public String photoUri;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    public String note;
}
