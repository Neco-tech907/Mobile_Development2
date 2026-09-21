package ru.mirea.ivanovrr.lapka.domain.repository;

import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Breed;

public interface BreedRepository {
    Breed getBreedByName(String breedName);
    List<Breed> getAllBreeds();
}
