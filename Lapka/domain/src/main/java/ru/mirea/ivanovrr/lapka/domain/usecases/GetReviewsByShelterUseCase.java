package ru.mirea.ivanovrr.lapka.domain.usecases;

import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Review;
import ru.mirea.ivanovrr.lapka.domain.repository.ReviewRepository;

/** Отзывы видят все, включая гостя. */
public class GetReviewsByShelterUseCase {
    private final ReviewRepository reviewRepository;

    public GetReviewsByShelterUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> execute(String shelterId) {
        return reviewRepository.getReviewsByShelter(shelterId);
    }
}
