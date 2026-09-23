package ru.mirea.ivanovrr.listviewapp;

/** Элемент списка: автор и название книги. */
public class Book {
    private final String author;
    private final String title;

    public Book(String author, String title) {
        this.author = author;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    /** ArrayAdapter по умолчанию выводит именно toString(). */
    @Override
    public String toString() {
        return author + " — " + title;
    }
}
