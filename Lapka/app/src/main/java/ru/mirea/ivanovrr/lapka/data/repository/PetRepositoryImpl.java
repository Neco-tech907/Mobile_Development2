package ru.mirea.ivanovrr.lapka.data.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Pet;
import ru.mirea.ivanovrr.lapka.domain.repository.PetRepository;

/** Тестовые данные. Позже — коллекция /pets в мок-API. */
public class PetRepositoryImpl implements PetRepository {

    private final List<Pet> pets = new ArrayList<>(Arrays.asList(
            new Pet("p1", "s1", "Рыжик", "Шпиц", 8, "Мальчик",
                    "https://picsum.photos/id/1062/400/300",
                    "Активный щенок, привит, готов к переезду."),
            new Pet("p2", "s1", "Белка", "Шпиц", 24, "Девочка",
                    "https://picsum.photos/id/1074/400/300",
                    "Спокойная, ладит с детьми и другими собаками."),
            new Pet("p3", "s1", "Джек", "Лабрадор", 14, "Мальчик",
                    "https://picsum.photos/id/1084/400/300",
                    "Любитдлинные прогулки, знает базовые команды."),
            new Pet("p4", "s2", "Соня", "Шпиц", 5, "Девочка",
                    "https://picsum.photos/id/40/400/300",
                    "Малышка из последнего помёта, с документами."),
            new Pet("p5", "s2", "Маркиз", "Мейн-кун", 18, "Мальчик",
                    "https://picsum.photos/id/593/400/300",
                    "Крупный кот, спокойный характер, приучен к лотку.")));

    @Override
    public List<Pet> getPetsByShelter(String shelterId) {
        List<Pet> result = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getShelterId().equals(shelterId)) {
                result.add(pet);
            }
        }
        return result;
    }

    @Override
    public Pet getPetById(String petId) {
        for (Pet pet : pets) {
            if (pet.getId().equals(petId)) {
                return pet;
            }
        }
        return null;
    }
}
