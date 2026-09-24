package ru.mirea.ivanovrr.retrofitapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/** Запросы к серверу описываются аннотациями; Retrofit сам подставит базовый адрес. */
public interface ApiService {

    @GET("todos")
    Call<List<Todo>> getTodos();

    /** Обновление дела: PUT /todos/{id} с телом в JSON. jsonplaceholder возвращает обновлённый объект. */
    @PUT("todos/{id}")
    Call<Todo> updateTodo(@Path("id") int id, @Body Todo todo);
}
