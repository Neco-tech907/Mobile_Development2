package ru.mirea.ivanovrr.lapka.data.storage.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Строка таблицы reviews (отзывы о приютах) в Room. */
@Entity(tableName = "reviews")
public class ReviewEntity {

    @PrimaryKey
    @NonNull
    public String id = "";

    @ColumnInfo(name = "shelter_id")
    public String shelterId;

    public String author;

    public int rating;

    public String text;

    @ColumnInfo(name = "created_at")
    public long createdAt;
}
