package ru.mirea.ivanovrr.lapka.domain.usecases;

import java.util.Collections;
import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Shelter;
import ru.mirea.ivanovrr.lapka.domain.repository.ShelterRepository;

public class GetSheltersByBreedUseCase {
    private final ShelterRepository shelterRepository;

    public GetSheltersByBreedUseCase(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public List<Shelter> execute(String breedName) {
        if (breedName == null || breedName.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return shelterRepository.getSheltersByBreed(breedName);
    }
}
