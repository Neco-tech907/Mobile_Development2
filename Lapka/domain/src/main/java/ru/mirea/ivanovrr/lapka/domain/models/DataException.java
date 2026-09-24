package ru.mirea.ivanovrr.lapka.domain.models;

/**
 * Ошибка получения данных (нет сети, сервер ответил ошибкой, база недоступна).
 * Объявлена в domain, чтобы presentation мог показать сообщение,
 * не зная, что внутри был Retrofit или Room. Unchecked — не засоряет сигнатуры use case'ов.
 */
public class DataException extends RuntimeException {
    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
