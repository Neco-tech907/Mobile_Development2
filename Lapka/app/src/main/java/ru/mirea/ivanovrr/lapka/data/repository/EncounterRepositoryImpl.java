package ru.mirea.ivanovrr.lapka.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Encounter;
import ru.mirea.ivanovrr.lapka.domain.repository.EncounterRepository;

/** Пока список в памяти. Позже — таблица encounters в Room. */
public class EncounterRepositoryImpl implements EncounterRepository {

    private final List<Encounter> encounters = new ArrayList<>();

    @Override
    public boolean saveEncounter(Encounter encounter) {
        return encounters.add(encounter);
    }

    @Override
    public List<Encounter> getEncounters() {
        return new ArrayList<>(encounters);
    }

    @Override
    public boolean deleteEncounter(String encounterId) {
        for (int i = 0; i < encounters.size(); i++) {
            if (encounters.get(i).getId().equals(encounterId)) {
                encounters.remove(i);
                return true;
            }
        }
        return false;
    }
}
