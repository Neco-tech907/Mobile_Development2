package ru.mirea.ivanovrr.lapka.domain.models;

/** Порода животного. Данные приходят из внешнего сервиса (TheDogAPI / TheCatAPI). */
public class Breed {
    private final String id;
    private final String name;
    private final String species;
    private final String imageUrl;
    private final String temperament;
    private final String description;

    public Breed(String id, String name, String species, String imageUrl,
                 String temperament, String description) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.imageUrl = imageUrl;
        this.temperament = temperament;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getImageUrl() { return imageUrl; }
    public String getTemperament() { return temperament; }
    public String getDescription() { return description; }
}
