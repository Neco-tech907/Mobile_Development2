package ru.mirea.ivanovrr.lapka.domain.usecases;

import java.util.Collections;
import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Encounter;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.EncounterRepository;

/** Гость альбома не имеет — возвращаем пустой список. */
public class GetEncountersUseCase {
    private final EncounterRepository encounterRepository;
    private final AuthRepository authRepository;

    public GetEncountersUseCase(EncounterRepository encounterRepository,
                                AuthRepository authRepository) {
        this.encounterRepository = encounterRepository;
        this.authRepository = authRepository;
    }

    public List<Encounter> execute() {
        if (authRepository.getCurrentUser() == null) {
            return Collections.emptyList();
        }
        return encounterRepository.getEncounters();
    }
}
