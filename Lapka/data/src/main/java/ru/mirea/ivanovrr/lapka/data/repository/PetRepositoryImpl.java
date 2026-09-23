package ru.mirea.ivanovrr.lapka.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ivanovrr.lapka.data.network.NetworkApi;
import ru.mirea.ivanovrr.lapka.data.network.NetworkException;
import ru.mirea.ivanovrr.lapka.data.network.dto.PetDto;
import ru.mirea.ivanovrr.lapka.domain.models.Pet;
import ru.mirea.ivanovrr.lapka.domain.repository.PetRepository;

/** Питомцы приютов берутся из NetworkApi (GET /pets). */
public class PetRepositoryImpl implements PetRepository {

    private final NetworkApi networkApi;

    public PetRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public List<Pet> getPetsByShelter(String shelterId) {
        List<Pet> result = new ArrayList<>();
        for (PetDto dto : loadPets()) {
            if (dto.shelterId.equals(shelterId)) {
                result.add(mapToDomain(dto));
            }
        }
        return result;
    }

    @Override
    public Pet getPetById(String petId) {
        for (PetDto dto : loadPets()) {
            if (dto.id.equals(petId)) {
                return mapToDomain(dto);
            }
        }
        return null;
    }

    private List<PetDto> loadPets() {
        try {
            return networkApi.getPets();
        } catch (NetworkException e) {
            return new ArrayList<>();
        }
    }

    private Pet mapToDomain(PetDto dto) {
        return new Pet(dto.id, dto.shelterId, dto.name, dto.breed, dto.ageMonths,
                dto.gender, dto.imageUrl, dto.description);
    }
}
