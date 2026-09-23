package ru.mirea.ivanovrr.lapka.data.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ivanovrr.lapka.data.network.dto.BreedDto;
import ru.mirea.ivanovrr.lapka.data.network.dto.PetDto;
import ru.mirea.ivanovrr.lapka.data.network.dto.ShelterDto;

/**
 * Сетевой источник данных с замоканными ответами сервера.
 * Каждый метод имитирует запрос: ждёт NETWORK_DELAY_MS, берёт «ответ» в виде JSON
 * и разбирает его в DTO — ровно так, как это будет с настоящим API.
 * Когда появится сервер, поменяется только тело методов (Retrofit вместо строк),
 * а репозитории и domain останутся как есть.
 * Методы блокирующие — вызываются только из фонового потока.
 */
public class NetworkApi {

    private static final long NETWORK_DELAY_MS = 400;

    // GET /breeds
    private static final String BREEDS_JSON = "["
            + "{\"id\": \"b1\", \"name\": \"Шпиц\", \"species\": \"Собака\", \"image_url\": \"https://cdn2.thedogapi.com/images/pomeranian.jpg\", \"temperament\": \"Активный, привязчивый, звонкий\", \"description\": \"Маленькая декоративная собака с густой шерстью. Требует регулярного вычёсывания и любит внимание хозяина.\"},"
            + "{\"id\": \"b2\", \"name\": \"Лабрадор\", \"species\": \"Собака\", \"image_url\": \"https://cdn2.thedogapi.com/images/labrador.jpg\", \"temperament\": \"Дружелюбный, энергичный, терпеливый\", \"description\": \"Крупная собака-компаньон. Отлично ладит с детьми, нуждается в длинных прогулках и физической нагрузке.\"},"
            + "{\"id\": \"b3\", \"name\": \"Мейн-кун\", \"species\": \"Кошка\", \"image_url\": \"https://cdn2.thecatapi.com/images/mainecoon.jpg\", \"temperament\": \"Спокойный, ласковый, общительный\", \"description\": \"Одна из самых крупных пород кошек. Хорошо переносит одиночество, любит высокие лежанки.\"}"
            + "]";

    // GET /shelters
    private static final String SHELTERS_JSON = "["
            + "{\"id\": \"s1\", \"name\": \"Приют «Верный друг»\", \"type\": \"Приют\", \"address\": \"Москва, ул. Лесная, 12\", \"phone\": \"+7 495 111-22-33\", \"working_hours\": \"10:00 – 19:00\", \"image_url\": \"https://picsum.photos/id/237/400/300\", \"breeds\": [\"Шпиц\", \"Лабрадор\"]},"
            + "{\"id\": \"s2\", \"name\": \"Зоомагазин «Лапки»\", \"type\": \"Магазин\", \"address\": \"Москва, пр-т Мира, 45\", \"phone\": \"+7 495 222-33-44\", \"working_hours\": \"09:00 – 21:00\", \"image_url\": \"https://picsum.photos/id/1025/400/300\", \"breeds\": [\"Шпиц\", \"Мейн-кун\"]},"
            + "{\"id\": \"s3\", \"name\": \"Приют «Добрые руки»\", \"type\": \"Приют\", \"address\": \"Москва, ул. Садовая, 7\", \"phone\": \"+7 495 333-44-55\", \"working_hours\": \"11:00 – 18:00\", \"image_url\": \"https://picsum.photos/id/169/400/300\", \"breeds\": [\"Лабрадор\", \"Мейн-кун\"]}"
            + "]";

    // GET /pets
    private static final String PETS_JSON = "["
            + "{\"id\": \"p1\", \"shelter_id\": \"s1\", \"name\": \"Рыжик\", \"breed\": \"Шпиц\", \"age_months\": 8, \"gender\": \"Мальчик\", \"image_url\": \"https://picsum.photos/id/1062/400/300\", \"description\": \"Активный щенок, привит, готов к переезду.\"},"
            + "{\"id\": \"p2\", \"shelter_id\": \"s1\", \"name\": \"Белка\", \"breed\": \"Шпиц\", \"age_months\": 24, \"gender\": \"Девочка\", \"image_url\": \"https://picsum.photos/id/1074/400/300\", \"description\": \"Спокойная, ладит с детьми и другими собаками.\"},"
            + "{\"id\": \"p3\", \"shelter_id\": \"s1\", \"name\": \"Джек\", \"breed\": \"Лабрадор\", \"age_months\": 14, \"gender\": \"Мальчик\", \"image_url\": \"https://picsum.photos/id/1084/400/300\", \"description\": \"Любит длинные прогулки, знает базовые команды.\"},"
            + "{\"id\": \"p4\", \"shelter_id\": \"s2\", \"name\": \"Соня\", \"breed\": \"Шпиц\", \"age_months\": 5, \"gender\": \"Девочка\", \"image_url\": \"https://picsum.photos/id/40/400/300\", \"description\": \"Малышка из последнего помёта, с документами.\"},"
            + "{\"id\": \"p5\", \"shelter_id\": \"s2\", \"name\": \"Маркиз\", \"breed\": \"Мейн-кун\", \"age_months\": 18, \"gender\": \"Мальчик\", \"image_url\": \"https://picsum.photos/id/593/400/300\", \"description\": \"Крупный кот, спокойный характер, приучен к лотку.\"},"
            + "{\"id\": \"p6\", \"shelter_id\": \"s3\", \"name\": \"Бублик\", \"breed\": \"Лабрадор\", \"age_months\": 36, \"gender\": \"Мальчик\", \"image_url\": \"https://picsum.photos/id/1012/400/300\", \"description\": \"Взрослый, воспитанный пёс, ходит на поводке без рывков.\"}"
            + "]";

    public List<BreedDto> getBreeds() throws NetworkException {
        JSONArray array = request(BREEDS_JSON);
        List<BreedDto> result = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                result.add(new BreedDto(o.getString("id"), o.getString("name"),
                        o.getString("species"), o.getString("image_url"),
                        o.getString("temperament"), o.getString("description")));
            }
        } catch (JSONException e) {
            throw new NetworkException("Некорректный ответ сервера /breeds", e);
        }
        return result;
    }

    public List<ShelterDto> getShelters() throws NetworkException {
        JSONArray array = request(SHELTERS_JSON);
        List<ShelterDto> result = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                JSONArray breedsArray = o.getJSONArray("breeds");
                List<String> breeds = new ArrayList<>();
                for (int j = 0; j < breedsArray.length(); j++) {
                    breeds.add(breedsArray.getString(j));
                }
                result.add(new ShelterDto(o.getString("id"), o.getString("name"),
                        o.getString("type"), o.getString("address"), o.getString("phone"),
                        o.getString("working_hours"), o.getString("image_url"), breeds));
            }
        } catch (JSONException e) {
            throw new NetworkException("Некорректный ответ сервера /shelters", e);
        }
        return result;
    }

    public List<PetDto> getPets() throws NetworkException {
        JSONArray array = request(PETS_JSON);
        List<PetDto> result = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                result.add(new PetDto(o.getString("id"), o.getString("shelter_id"),
                        o.getString("name"), o.getString("breed"), o.getInt("age_months"),
                        o.getString("gender"), o.getString("image_url"),
                        o.getString("description")));
            }
        } catch (JSONException e) {
            throw new NetworkException("Некорректный ответ сервера /pets", e);
        }
        return result;
    }

    /** Имитация HTTP-запроса: задержка сети + «тело ответа». */
    private JSONArray request(String responseBody) throws NetworkException {
        try {
            Thread.sleep(NETWORK_DELAY_MS);
            return new JSONArray(responseBody);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NetworkException("Запрос прерван", e);
        } catch (JSONException e) {
            throw new NetworkException("Не удалось разобрать ответ сервера", e);
        }
    }
}
