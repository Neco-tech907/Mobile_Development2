package ru.mirea.ivanovrr.lapka.data.network.dto;

/** Питомец приюта в том виде, в каком его отдаёт сервер. */
public class PetDto {
    public final String id;
    public final String shelterId;
    public final String name;
    public final String breed;
    public final int ageMonths;
    public final String gender;
    public final String imageUrl;
    public final String description;

    public PetDto(String id, String shelterId, String name, String breed, int ageMonths,
                  String gender, String imageUrl, String description) {
        this.id = id;
        this.shelterId = shelterId;
        this.name = name;
        this.breed = breed;
        this.ageMonths = ageMonths;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
