package ru.mirea.ivanovrr.lapka.data.network.dto;

/** Порода в том виде, в каком её отдаёт сервер (поле image_url и т.п.). */
public class BreedDto {
    public final String id;
    public final String name;
    public final String species;
    public final String imageUrl;
    public final String temperament;
    public final String description;

    public BreedDto(String id, String name, String species, String imageUrl,
                    String temperament, String description) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.imageUrl = imageUrl;
        this.temperament = temperament;
        this.description = description;
    }
}
