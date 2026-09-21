package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.Shelter;
import ru.mirea.ivanovrr.lapka.domain.repository.ShelterRepository;

public class GetShelterDetailsUseCase {
    private final ShelterRepository shelterRepository;

    public GetShelterDetailsUseCase(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public Shelter execute(String shelterId) {
        if (shelterId == null || shelterId.trim().isEmpty()) {
            return null;
        }
        return shelterRepository.getShelterById(shelterId);
    }
}
