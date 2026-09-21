package ru.mirea.ivanovrr.lapka.domain.models;

/** Отзыв пользователя о приюте. */
public class Review {
    private final String id;
    private final String shelterId;
    private final String author;
    private final int rating;
    private final String text;
    private final long dateMillis;

    public Review(String id, String shelterId, String author, int rating, String text,
                  long dateMillis) {
        this.id = id;
        this.shelterId = shelterId;
        this.author = author;
        this.rating = rating;
        this.text = text;
        this.dateMillis = dateMillis;
    }

    public String getId() { return id; }
    public String getShelterId() { return shelterId; }
    public String getAuthor() { return author; }
    public int getRating() { return rating; }
    public String getText() { return text; }
    public long getDateMillis() { return dateMillis; }
}
