package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.RecognitionResult;
import ru.mirea.ivanovrr.lapka.domain.repository.BreedRecognitionRepository;

/** Распознавание породы по фото. Порог уверенности — бизнес-правило слоя domain. */
public class RecognizeBreedUseCase {
    private static final float MIN_CONFIDENCE = 0.5f;

    private final BreedRecognitionRepository recognitionRepository;

    public RecognizeBreedUseCase(BreedRecognitionRepository recognitionRepository) {
        this.recognitionRepository = recognitionRepository;
    }

    public RecognitionResult execute(String photoUri) {
        if (photoUri == null || photoUri.trim().isEmpty()) {
            return null;
        }
        RecognitionResult result = recognitionRepository.recognize(photoUri);
        if (result == null || result.getConfidence() < MIN_CONFIDENCE) {
            return null;
        }
        return result;
    }
}
