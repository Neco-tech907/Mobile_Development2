package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.Breed;
import ru.mirea.ivanovrr.lapka.domain.repository.BreedRepository;

public class GetBreedInfoUseCase {
    private final BreedRepository breedRepository;

    public GetBreedInfoUseCase(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    public Breed execute(String breedName) {
        if (breedName == null || breedName.trim().isEmpty()) {
            return null;
        }
        return breedRepository.getBreedByName(breedName);
    }
}
