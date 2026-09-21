package ru.mirea.ivanovrr.lapka.data.repository;

import ru.mirea.ivanovrr.lapka.domain.models.RecognitionResult;
import ru.mirea.ivanovrr.lapka.domain.repository.BreedRecognitionRepository;

/**
 * Пока всегда отвечает «Шпиц». Позже сюда придёт TensorFlow Lite:
 * загрузка .tflite модели и инференс по Bitmap с камеры.
 */
public class BreedRecognitionRepositoryImpl implements BreedRecognitionRepository {

    @Override
    public RecognitionResult recognize(String photoUri) {
        return new RecognitionResult("Шпиц", 0.87f);
    }
}
