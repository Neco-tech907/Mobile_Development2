package ru.mirea.ivanovrr.lapka.data.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Breed;
import ru.mirea.ivanovrr.lapka.domain.repository.BreedRepository;

/** Тестовые данные. Позже — Retrofit к thedogapi.com / thecatapi.com. */
public class BreedRepositoryImpl implements BreedRepository {

    private final List<Breed> breeds = new ArrayList<>(Arrays.asList(
            new Breed("b1", "Шпиц", "Собака",
                    "https://cdn2.thedogapi.com/images/pomeranian.jpg",
                    "Активный, привязчивый, звонкий",
                    "Маленькая декоративная собака с густой шерстью. Требует регулярного "
                            + "вычёсывания и любит внимание хозяина."),
            new Breed("b2", "Лабрадор", "Собака",
                    "https://cdn2.thedogapi.com/images/labrador.jpg",
                    "Дружелюбный, Энергичный, терпеливый",
                    "Крупная собака-компаньон. Отлично ладит с детьми, нуждается в длинных "
                            + "прогулках и физической нагрузке."),
            new Breed("b3", "Мейн-кун", "Кошка",
                    "https://cdn2.thecatapi.com/images/mainecoon.jpg",
                    "Спокойный, ласковый, общительный",
                    "Одна из самых крупных пород кошек. Хорошо переносит одиночество, "
                            + "любит высокие лежанки.")));

    @Override
    public Breed getBreedByName(String breedName) {
        for (Breed breed : breeds) {
            if (breed.getName().equalsIgnoreCase(breedName)) {
                return breed;
            }
        }
        return null;
    }

    @Override
    public List<Breed> getAllBreeds() {
        return new ArrayList<>(breeds);
    }
}
