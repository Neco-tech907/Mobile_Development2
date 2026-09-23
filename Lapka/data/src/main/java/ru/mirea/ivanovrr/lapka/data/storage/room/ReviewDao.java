package ru.mirea.ivanovrr.lapka.data.storage.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.mirea.ivanovrr.lapka.data.storage.models.ReviewEntity;

@Dao
public interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ReviewEntity review);

    @Query("SELECT * FROM reviews WHERE shelter_id = :shelterId ORDER BY created_at DESC")
    List<ReviewEntity> getByShelter(String shelterId);
}
