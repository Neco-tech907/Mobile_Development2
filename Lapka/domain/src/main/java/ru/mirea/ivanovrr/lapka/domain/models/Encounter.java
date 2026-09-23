package ru.mirea.ivanovrr.lapka.domain.models;

/** Встреча: сохранённое пользователем распознавание животного. */
public class Encounter {
    private final String id;
    private final String breedName;
    private final String photoUri;
    private final long dateMillis;
    private final String note;

    public Encounter(String id, String breedName, String photoUri, long dateMillis, String note) {
        this.id = id;
        this.breedName = breedName;
        this.photoUri = photoUri;
        this.dateMillis = dateMillis;
        this.note = note;
    }

    public String getId() { return id; }
    public String getBreedName() { return breedName; }
    public String getPhotoUri() { return photoUri; }
    public long getDateMillis() { return dateMillis; }
    public String getNote() { return note; }
}
