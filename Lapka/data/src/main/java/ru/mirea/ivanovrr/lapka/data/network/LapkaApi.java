package ru.mirea.ivanovrr.lapka.data.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

import ru.mirea.ivanovrr.lapka.data.network.dto.BreedDto;
import ru.mirea.ivanovrr.lapka.data.network.dto.PetDto;
import ru.mirea.ivanovrr.lapka.data.network.dto.ShelterDto;

/**
 * Описание запросов к серверу Лапки. Сервер — JSON-файлы в репозитории проекта
 * (raw.githubusercontent.com), базовый адрес задаётся в NetworkApi.
 */
public interface LapkaApi {

    @GET("breeds.json")
    Call<List<BreedDto>> getBreeds();

    @GET("shelters.json")
    Call<List<ShelterDto>> getShelters();

    @GET("pets.json")
    Call<List<PetDto>> getPets();
}
