package ru.mirea.ivanovrr.lapka.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ivanovrr.lapka.data.network.NetworkApi;
import ru.mirea.ivanovrr.lapka.data.network.NetworkException;
import ru.mirea.ivanovrr.lapka.data.network.dto.ShelterDto;
import ru.mirea.ivanovrr.lapka.domain.models.DataException;
import ru.mirea.ivanovrr.lapka.domain.models.Shelter;
import ru.mirea.ivanovrr.lapka.domain.repository.ShelterRepository;

/** Приюты и магазины берутся из NetworkApi (GET /shelters). */
public class ShelterRepositoryImpl implements ShelterRepository {

    private final NetworkApi networkApi;

    public ShelterRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public List<Shelter> getSheltersByBreed(String breedName) {
        List<Shelter> result = new ArrayList<>();
        for (ShelterDto dto : loadShelters()) {
            for (String breed : dto.breeds) {
                if (breed.equalsIgnoreCase(breedName)) {
                    result.add(mapToDomain(dto));
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public Shelter getShelterById(String shelterId) {
        for (ShelterDto dto : loadShelters()) {
            if (dto.id.equals(shelterId)) {
                return mapToDomain(dto);
            }
        }
        return null;
    }

    private List<ShelterDto> loadShelters() {
        try {
            return networkApi.getShelters();
        } catch (NetworkException e) {
            throw new DataException(e.getMessage(), e);
        }
    }

    private Shelter mapToDomain(ShelterDto dto) {
        return new Shelter(dto.id, dto.name, dto.type, dto.address, dto.phone,
                dto.workingHours, dto.imageUrl, new ArrayList<>(dto.breeds));
    }
}
