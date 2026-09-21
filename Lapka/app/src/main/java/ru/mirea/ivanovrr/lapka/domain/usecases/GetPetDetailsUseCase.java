package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.Pet;
import ru.mirea.ivanovrr.lapka.domain.repository.PetRepository;

public class GetPetDetailsUseCase {
    private final PetRepository petRepository;

    public GetPetDetailsUseCase(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet execute(String petId) {
        if (petId == null || petId.trim().isEmpty()) {
            return null;
        }
        return petRepository.getPetById(petId);
    }
}
