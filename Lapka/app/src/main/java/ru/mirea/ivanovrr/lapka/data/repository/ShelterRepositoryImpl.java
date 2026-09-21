package ru.mirea.ivanovrr.lapka.data.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Shelter;
import ru.mirea.ivanovrr.lapka.domain.repository.ShelterRepository;

/** Тестовые данные. Позже — свой мок-API на mockapi.io. */
public class ShelterRepositoryImpl implements ShelterRepository {

    private final List<Shelter> shelters = new ArrayList<>(Arrays.asList(
            new Shelter("s1", "Приют «Верный друг»", "Приют",
                    "Москва, ул. Лесная, 12", "+7 495 111-22-33", "10:00 – 19:00",
                    "https://picsum.photos/id/237/400/300",
                    Arrays.asList("Шпиц", "Лабрадор")),
            new Shelter("s2", "Зоомагазин «Лапки»", "Магазин",
                    "Москва, пр-т Мира, 45", "+7 495 222-33-44", "09:00 – 21:00",
                    "https://picsum.photos/id/1025/400/300",
                    Arrays.asList("Шпиц", "Мейн-кун")),
            new Shelter("s3", "Приют «Добрые руки»", "Приют",
                    "Москва, ул. Садовая, 7", "+7 495 333-44-55", "11:00 – 18:00",
                    "https://picsum.photos/id/169/400/300",
                    Arrays.asList("Лабрадор", "Мейн-кун"))));

    @Override
    public List<Shelter> getSheltersByBreed(String breedName) {
        List<Shelter> result = new ArrayList<>();
        for (Shelter shelter : shelters) {
            for (String breed : shelter.getAvailableBreeds()) {
                if (breed.equalsIgnoreCase(breedName)) {
                    result.add(shelter);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public Shelter getShelterById(String shelterId) {
        for (Shelter shelter : shelters) {
            if (shelter.getId().equals(shelterId)) {
                return shelter;
            }
        }
        return null;
    }
}
