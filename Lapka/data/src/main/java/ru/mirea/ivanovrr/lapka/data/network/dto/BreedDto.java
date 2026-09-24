package ru.mirea.ivanovrr.lapka.data.network.dto;

import com.google.gson.annotations.SerializedName;

/** Порода в том виде, в каком её отдаёт сервер (поле image_url и т.п.). Заполняет Gson. */
public class BreedDto {
    @SerializedName("id") public String id;
    @SerializedName("name") public String name;
    @SerializedName("species") public String species;
    @SerializedName("image_url") public String imageUrl;
    @SerializedName("temperament") public String temperament;
    @SerializedName("description") public String description;
}
