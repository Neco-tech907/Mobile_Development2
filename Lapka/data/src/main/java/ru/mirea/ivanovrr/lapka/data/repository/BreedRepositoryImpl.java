package ru.mirea.ivanovrr.lapka.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ivanovrr.lapka.data.network.NetworkApi;
import ru.mirea.ivanovrr.lapka.data.network.NetworkException;
import ru.mirea.ivanovrr.lapka.data.network.dto.BreedDto;
import ru.mirea.ivanovrr.lapka.domain.models.Breed;
import ru.mirea.ivanovrr.lapka.domain.repository.BreedRepository;

/** Справочник пород берётся из NetworkApi (GET /breeds). */
public class BreedRepositoryImpl implements BreedRepository {

    private final NetworkApi networkApi;

    public BreedRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public Breed getBreedByName(String breedName) {
        for (Breed breed : getAllBreeds()) {
            if (breed.getName().equalsIgnoreCase(breedName)) {
                return breed;
            }
        }
        return null;
    }

    @Override
    public List<Breed> getAllBreeds() {
        List<Breed> result = new ArrayList<>();
        try {
            for (BreedDto dto : networkApi.getBreeds()) {
                result.add(mapToDomain(dto));
            }
        } catch (NetworkException e) {
            // сеть недоступна — отдаём пустой список, экран покажет «ничего не найдено»
        }
        return result;
    }

    private Breed mapToDomain(BreedDto dto) {
        return new Breed(dto.id, dto.name, dto.species, dto.imageUrl,
                dto.temperament, dto.description);
    }
}
