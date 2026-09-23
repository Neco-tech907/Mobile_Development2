package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.repository.EncounterRepository;

public class DeleteEncounterUseCase {
    private final EncounterRepository encounterRepository;

    public DeleteEncounterUseCase(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    public boolean execute(String encounterId) {
        if (encounterId == null || encounterId.trim().isEmpty()) {
            return false;
        }
        return encounterRepository.deleteEncounter(encounterId);
    }
}
