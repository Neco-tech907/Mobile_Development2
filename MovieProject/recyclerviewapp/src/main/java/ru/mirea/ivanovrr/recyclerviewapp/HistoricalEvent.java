package ru.mirea.ivanovrr.recyclerviewapp;

/**
 * Модель элемента списка: год, название, краткое описание
 * и имя drawable-ресурса с эмблемой (ищется через getIdentifier, как в методичке).
 */
public class HistoricalEvent {
    private final int year;
    private final String title;
    private final String description;
    private final String imageName;

    public HistoricalEvent(int year, String title, String description, String imageName) {
        this.year = year;
        this.title = title;
        this.description = description;
        this.imageName = imageName;
    }

    public int getYear() { return year; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageName() { return imageName; }

    @Override
    public String toString() {
        return year + " — " + title;
    }
}
