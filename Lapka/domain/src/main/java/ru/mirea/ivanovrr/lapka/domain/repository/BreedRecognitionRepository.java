package ru.mirea.ivanovrr.lapka.domain.repository;

import ru.mirea.ivanovrr.lapka.domain.models.RecognitionResult;

/** За этим интерфейсом позже спрячется модель TensorFlow Lite. */
public interface BreedRecognitionRepository {
    RecognitionResult recognize(String photoUri);
}
