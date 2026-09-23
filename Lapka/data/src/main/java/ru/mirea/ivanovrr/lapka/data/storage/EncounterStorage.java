package ru.mirea.ivanovrr.lapka.data.storage;

import java.util.List;

import ru.mirea.ivanovrr.lapka.data.storage.models.EncounterEntity;

/** Хранилище альбома встреч. Реализация — RoomEncounterStorage. */
public interface EncounterStorage {
    boolean save(EncounterEntity encounter);
    List<EncounterEntity> getAll();
    boolean delete(String id);
}
