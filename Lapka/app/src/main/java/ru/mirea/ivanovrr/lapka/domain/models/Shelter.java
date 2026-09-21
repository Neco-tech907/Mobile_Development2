package ru.mirea.ivanovrr.lapka.domain.models;

import java.util.List;

/** Приют или зоомагазин. */
public class Shelter {
    private final String id;
    private final String name;
    private final String type;
    private final String address;
    private final String phone;
    private final String workingHours;
    private final String imageUrl;
    private final List<String> availableBreeds;

    public Shelter(String id, String name, String type, String address, String phone,
                   String workingHours, String imageUrl, List<String> availableBreeds) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.address = address;
        this.phone = phone;
        this.workingHours = workingHours;
        this.imageUrl = imageUrl;
        this.availableBreeds = availableBreeds;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getWorkingHours() { return workingHours; }
    public String getImageUrl() { return imageUrl; }
    public List<String> getAvailableBreeds() { return availableBreeds; }
}
