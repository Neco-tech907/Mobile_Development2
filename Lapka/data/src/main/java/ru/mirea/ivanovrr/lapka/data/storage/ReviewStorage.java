package ru.mirea.ivanovrr.lapka.data.storage;

import java.util.List;

import ru.mirea.ivanovrr.lapka.data.storage.models.ReviewEntity;

/** Хранилище отзывов. Реализация — RoomReviewStorage. */
public interface ReviewStorage {
    boolean save(ReviewEntity review);
    List<ReviewEntity> getByShelter(String shelterId);
}
