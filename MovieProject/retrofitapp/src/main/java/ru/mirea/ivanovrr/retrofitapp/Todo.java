package ru.mirea.ivanovrr.retrofitapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/** POJO для ответа https://jsonplaceholder.typicode.com/todos — поля совпадают с JSON. */
public class Todo {

    @SerializedName("userId")
    @Expose
    private Integer userId;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("completed")
    @Expose
    private Boolean completed;

    public Integer getUserId() { return userId; }
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public Boolean getCompleted() { return completed != null && completed; }

    public void setCompleted(Boolean completed) { this.completed = completed; }

    /** Картинка для дела: детерминированная по id, чтобы Picasso кэшировал её между запусками. */
    public String getImageUrl() {
        return "https://picsum.photos/seed/todo" + id + "/300/300";
    }
}
