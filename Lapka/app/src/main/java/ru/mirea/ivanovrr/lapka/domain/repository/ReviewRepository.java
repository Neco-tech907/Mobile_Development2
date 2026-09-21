package ru.mirea.ivanovrr.lapka.domain.repository;

import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Review;

public interface ReviewRepository {
    boolean addReview(Review review);
    List<Review> getReviewsByShelter(String shelterId);
}
