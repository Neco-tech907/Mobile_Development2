package ru.mirea.ivanovrr.lapka.data.storage.room;

import android.content.Context;

import java.util.List;

import ru.mirea.ivanovrr.lapka.data.storage.ReviewStorage;
import ru.mirea.ivanovrr.lapka.data.storage.models.ReviewEntity;

/** Отзывы о приютах в Room. Методы блокирующие — вызываются из фонового потока. */
public class RoomReviewStorage implements ReviewStorage {

    private final ReviewDao dao;

    public RoomReviewStorage(Context context) {
        this.dao = LapkaDatabase.getInstance(context).reviewDao();
    }

    @Override
    public boolean save(ReviewEntity review) {
        return dao.insert(review) != -1;
    }

    @Override
    public List<ReviewEntity> getByShelter(String shelterId) {
        return dao.getByShelter(shelterId);
    }
}
