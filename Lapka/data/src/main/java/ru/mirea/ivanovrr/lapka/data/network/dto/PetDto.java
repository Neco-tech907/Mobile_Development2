package ru.mirea.ivanovrr.lapka.data.network.dto;

import com.google.gson.annotations.SerializedName;

/** Питомец приюта в том виде, в каком его отдаёт сервер. Заполняет Gson. */
public class PetDto {
    @SerializedName("id") public String id;
    @SerializedName("shelter_id") public String shelterId;
    @SerializedName("name") public String name;
    @SerializedName("breed") public String breed;
    @SerializedName("age_months") public int ageMonths;
    @SerializedName("gender") public String gender;
    @SerializedName("image_url") public String imageUrl;
    @SerializedName("description") public String description;
}
