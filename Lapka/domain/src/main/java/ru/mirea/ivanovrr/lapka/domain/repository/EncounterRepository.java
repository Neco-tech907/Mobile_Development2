package ru.mirea.ivanovrr.lapka.domain.repository;

import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Encounter;

public interface EncounterRepository {
    boolean saveEncounter(Encounter encounter);
    List<Encounter> getEncounters();
    boolean deleteEncounter(String encounterId);
}
