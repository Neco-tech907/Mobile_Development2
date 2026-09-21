package ru.mirea.ivanovrr.lapka.domain.models;

/** Результат работы модели TensorFlow Lite: порода и уверенность от 0 до 1. */
public class RecognitionResult {
    private final String breedName;
    private final float confidence;

    public RecognitionResult(String breedName, float confidence) {
        this.breedName = breedName;
        this.confidence = confidence;
    }

    public String getBreedName() { return breedName; }
    public float getConfidence() { return confidence; }
}
