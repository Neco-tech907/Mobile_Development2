package ru.mirea.ivanovrr.lapka.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ivanovrr.lapka.data.storage.EncounterStorage;
import ru.mirea.ivanovrr.lapka.data.storage.models.EncounterEntity;
import ru.mirea.ivanovrr.lapka.domain.models.Encounter;
import ru.mirea.ivanovrr.lapka.domain.repository.EncounterRepository;

/** Альбом встреч: доменная модель Encounter <-> EncounterEntity в Room. */
public class EncounterRepositoryImpl implements EncounterRepository {

    private final EncounterStorage encounterStorage;

    public EncounterRepositoryImpl(EncounterStorage encounterStorage) {
        this.encounterStorage = encounterStorage;
    }

    @Override
    public boolean saveEncounter(Encounter encounter) {
        return encounterStorage.save(mapToStorage(encounter));
    }

    @Override
    public List<Encounter> getEncounters() {
        List<Encounter> result = new ArrayList<>();
        for (EncounterEntity entity : encounterStorage.getAll()) {
            result.add(mapToDomain(entity));
        }
        return result;
    }

    @Override
    public boolean deleteEncounter(String encounterId) {
        return encounterStorage.delete(encounterId);
    }

    private EncounterEntity mapToStorage(Encounter encounter) {
        EncounterEntity entity = new EncounterEntity();
        entity.id = encounter.getId();
        entity.breedName = encounter.getBreedName();
        entity.photoUri = encounter.getPhotoUri();
        entity.createdAt = encounter.getDateMillis();
        entity.note = encounter.getNote();
        return entity;
    }

    private Encounter mapToDomain(EncounterEntity entity) {
        return new Encounter(entity.id, entity.breedName, entity.photoUri,
                entity.createdAt, entity.note);
    }
}
