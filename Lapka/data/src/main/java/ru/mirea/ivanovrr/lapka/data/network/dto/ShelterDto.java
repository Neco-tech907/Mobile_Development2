package ru.mirea.ivanovrr.lapka.data.network.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Приют или магазин в том виде, в каком его отдаёт сервер. Заполняет Gson. */
public class ShelterDto {
    @SerializedName("id") public String id;
    @SerializedName("name") public String name;
    @SerializedName("type") public String type;
    @SerializedName("address") public String address;
    @SerializedName("phone") public String phone;
    @SerializedName("working_hours") public String workingHours;
    @SerializedName("image_url") public String imageUrl;
    @SerializedName("breeds") public List<String> breeds;
}
