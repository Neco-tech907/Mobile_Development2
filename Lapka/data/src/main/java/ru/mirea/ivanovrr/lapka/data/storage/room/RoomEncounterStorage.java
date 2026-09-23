package ru.mirea.ivanovrr.lapka.data.storage.room;

import android.content.Context;

import java.util.List;

import ru.mirea.ivanovrr.lapka.data.storage.EncounterStorage;
import ru.mirea.ivanovrr.lapka.data.storage.models.EncounterEntity;

/** Альбом встреч в Room. Методы блокирующие — вызываются из фонового потока. */
public class RoomEncounterStorage implements EncounterStorage {

    private final EncounterDao dao;

    public RoomEncounterStorage(Context context) {
        this.dao = LapkaDatabase.getInstance(context).encounterDao();
    }

    @Override
    public boolean save(EncounterEntity encounter) {
        return dao.insert(encounter) != -1;
    }

    @Override
    public List<EncounterEntity> getAll() {
        return dao.getAll();
    }

    @Override
    public boolean delete(String id) {
        return dao.deleteById(id) > 0;
    }
}
