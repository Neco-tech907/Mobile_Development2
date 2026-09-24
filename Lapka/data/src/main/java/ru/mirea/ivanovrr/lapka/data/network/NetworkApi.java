package ru.mirea.ivanovrr.lapka.data.network;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import ru.mirea.ivanovrr.lapka.data.network.dto.BreedDto;
import ru.mirea.ivanovrr.lapka.data.network.dto.PetDto;
import ru.mirea.ivanovrr.lapka.data.network.dto.ShelterDto;

/**
 * Сетевой источник данных на Retrofit. До практики 5 здесь были захардкоженные JSON-строки;
 * теперь те же JSON лежат в репозитории проекта (папка Lapka/api) и скачиваются по сети.
 * Сигнатуры методов не изменились, поэтому репозитории и domain остались прежними.
 * Методы блокирующие (Call.execute) — вызываются только из фонового потока.
 */
public class NetworkApi {

    public static final String BASE_URL =
            "https://raw.githubusercontent.com/Neco-tech907/Mobile_Development2/main/Lapka/api/";

    private final LapkaApi api;

    public NetworkApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(LapkaApi.class);
    }

    public List<BreedDto> getBreeds() throws NetworkException {
        return execute(api.getBreeds(), "/breeds");
    }

    public List<ShelterDto> getShelters() throws NetworkException {
        return execute(api.getShelters(), "/shelters");
    }

    public List<PetDto> getPets() throws NetworkException {
        return execute(api.getPets(), "/pets");
    }

    /**
     * Синхронный запрос с разбором ошибок: нет сети — IOException,
     * сервер ответил не 2xx — код ответа, пустое тело — тоже ошибка.
     */
    private <T> T execute(Call<T> call, String endpoint) throws NetworkException {
        Response<T> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new NetworkException("Нет подключения к интернету (" + endpoint + ")", e);
        } catch (RuntimeException e) {
            // Gson не смог разобрать JSON или неверный адрес
            throw new NetworkException("Некорректный ответ сервера " + endpoint, e);
        }
        if (!response.isSuccessful()) {
            throw new NetworkException("Сервер ответил ошибкой " + response.code()
                    + " (" + endpoint + ")", null);
        }
        if (response.body() == null) {
            throw new NetworkException("Пустой ответ сервера " + endpoint, null);
        }
        return response.body();
    }
}
