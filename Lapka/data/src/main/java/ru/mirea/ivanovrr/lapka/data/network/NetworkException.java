package ru.mirea.ivanovrr.lapka.data.network;

/** Ошибка сетевого запроса или разбора ответа. */
public class NetworkException extends Exception {
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
