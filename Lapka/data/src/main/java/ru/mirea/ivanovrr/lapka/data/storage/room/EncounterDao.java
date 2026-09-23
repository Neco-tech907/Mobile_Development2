package ru.mirea.ivanovrr.lapka.data.storage.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.mirea.ivanovrr.lapka.data.storage.models.EncounterEntity;

@Dao
public interface EncounterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(EncounterEntity encounter);

    @Query("SELECT * FROM encounters ORDER BY created_at DESC")
    List<EncounterEntity> getAll();

    @Query("DELETE FROM encounters WHERE id = :id")
    int deleteById(String id);
}
