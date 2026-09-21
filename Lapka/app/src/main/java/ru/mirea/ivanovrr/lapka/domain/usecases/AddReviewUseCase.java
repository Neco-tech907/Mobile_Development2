package ru.mirea.ivanovrr.lapka.domain.usecases;

import ru.mirea.ivanovrr.lapka.domain.models.Review;
import ru.mirea.ivanovrr.lapka.domain.repository.AuthRepository;
import ru.mirea.ivanovrr.lapka.domain.repository.ReviewRepository;

/** Отзыв оставляет только авторизованный, оценка — от 1 до 5. */
public class AddReviewUseCase {
    private final ReviewRepository reviewRepository;
    private final AuthRepository authRepository;

    public AddReviewUseCase(ReviewRepository reviewRepository, AuthRepository authRepository) {
        this.reviewRepository = reviewRepository;
        this.authRepository = authRepository;
    }

    public boolean execute(Review review) {
        if (authRepository.getCurrentUser() == null) {
            return false;
        }
        if (review == null || review.getRating() < 1 || review.getRating() > 5) {
            return false;
        }
        if (review.getText() == null || review.getText().trim().isEmpty()) {
            return false;
        }
        return reviewRepository.addReview(review);
    }
}
