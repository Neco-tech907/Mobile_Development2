package ru.mirea.ivanovrr.lapka.domain.repository;

import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Pet;

public interface PetRepository {
    List<Pet> getPetsByShelter(String shelterId);
    Pet getPetById(String petId);
}
