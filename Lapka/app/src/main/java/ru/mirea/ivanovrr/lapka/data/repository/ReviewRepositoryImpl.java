package ru.mirea.ivanovrr.lapka.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ivanovrr.lapka.domain.models.Review;
import ru.mirea.ivanovrr.lapka.domain.repository.ReviewRepository;

/** Пока список в памяти. Позже — таблица reviews в Room. */
public class ReviewRepositoryImpl implements ReviewRepository {

    private final List<Review> reviews = new ArrayList<>();

    @Override
    public boolean addReview(Review review) {
        return reviews.add(review);
    }

    @Override
    public List<Review> getReviewsByShelter(String shelterId) {
        List<Review> result = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getShelterId().equals(shelterId)) {
                result.add(review);
            }
        }
        return result;
    }
}
