package ru.mirea.ivanovrr.lapka.domain.models;

/** Конкретное животное в приюте или магазине. */
public class Pet {
    private final String id;
    private final String shelterId;
    private final String name;
    private final String breedName;
    private final int ageMonths;
    private final String gender;
    private final String imageUrl;
    private final String description;

    public Pet(String id, String shelterId, String name, String breedName, int ageMonths,
               String gender, String imageUrl, String description) {
        this.id = id;
        this.shelterId = shelterId;
        this.name = name;
        this.breedName = breedName;
        this.ageMonths = ageMonths;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getId() { return id; }
    public String getShelterId() { return shelterId; }
    public String getName() { return name; }
    public String getBreedName() { return breedName; }
    public int getAgeMonths() { return ageMonths; }
    public String getGender() { return gender; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
}
