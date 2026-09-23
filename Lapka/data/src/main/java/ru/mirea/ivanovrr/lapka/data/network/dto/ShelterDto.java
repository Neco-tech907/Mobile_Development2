package ru.mirea.ivanovrr.lapka.data.network.dto;

import java.util.List;

/** Приют или магазин в том виде, в каком его отдаёт сервер. */
public class ShelterDto {
    public final String id;
    public final String name;
    public final String type;
    public final String address;
    public final String phone;
    public final String workingHours;
    public final String imageUrl;
    public final List<String> breeds;

    public ShelterDto(String id, String name, String type, String address, String phone,
                      String workingHours, String imageUrl, List<String> breeds) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.address = address;
        this.phone = phone;
        this.workingHours = workingHours;
        this.imageUrl = imageUrl;
        this.breeds = breeds;
    }
}
