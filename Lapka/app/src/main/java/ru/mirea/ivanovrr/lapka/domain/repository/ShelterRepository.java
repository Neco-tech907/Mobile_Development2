package ru.mirea.ivanovrr.lapka.domain.repository;

import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Shelter;

public interface ShelterRepository {
    List<Shelter> getSheltersByBreed(String breedName);
    Shelter getShelterById(String shelterId);
}
