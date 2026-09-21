package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.Encounter;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.EncounterRepository;

/** Сохранять встречи может только авторизованный пользователь — это правило слоя domain. */
public class SaveEncounterUseCase {
    private final EncounterRepository encounterRepository;
    private final AuthRepository authRepository;

    public SaveEncounterUseCase(EncounterRepository encounterRepository,
                                AuthRepository authRepository) {
        this.encounterRepository = encounterRepository;
        this.authRepository = authRepository;
    }

    public boolean execute(Encounter encounter) {
        if (authRepository.getCurrentUser() == null) {
            return false;
        }
        if (encounter == null || encounter.getBreedName() == null
                || encounter.getBreedName().trim().isEmpty()) {
            return false;
        }
        return encounterRepository.saveEncounter(encounter);
    }
}
