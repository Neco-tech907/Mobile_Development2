package ru.mirea.ivanovrr.lapka.domain.usecases;

import java.util.Collections;
import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Pet;
import ru.mirea.ivanovrr.lapka.domain.repository.PetRepository;

public class GetPetsByShelterUseCase {
    private final PetRepository petRepository;

    public GetPetsByShelterUseCase(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> execute(String shelterId) {
        if (shelterId == null || shelterId.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return petRepository.getPetsByShelter(shelterId);
    }
}
