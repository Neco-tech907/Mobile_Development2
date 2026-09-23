package ru.mirea.ivanovrr.lapka.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ivanovrr.lapka.data.storage.ReviewStorage;
import ru.mirea.ivanovrr.lapka.data.storage.models.ReviewEntity;
import ru.mirea.ivanovrr.lapka.domain.models.Review;
import ru.mirea.ivanovrr.lapka.domain.repository.ReviewRepository;

/** Отзывы о приютах: доменная модель Review <-> ReviewEntity в Room. */
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewStorage reviewStorage;

    public ReviewRepositoryImpl(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    @Override
    public boolean addReview(Review review) {
        return reviewStorage.save(mapToStorage(review));
    }

    @Override
    public List<Review> getReviewsByShelter(String shelterId) {
        List<Review> result = new ArrayList<>();
        for (ReviewEntity entity : reviewStorage.getByShelter(shelterId)) {
            result.add(mapToDomain(entity));
        }
        return result;
    }

    private ReviewEntity mapToStorage(Review review) {
        ReviewEntity entity = new ReviewEntity();
        entity.id = review.getId();
        entity.shelterId = review.getShelterId();
        entity.author = review.getAuthor();
        entity.rating = review.getRating();
        entity.text = review.getText();
        entity.createdAt = review.getDateMillis();
        return entity;
    }

    private Review mapToDomain(ReviewEntity entity) {
        return new Review(entity.id, entity.shelterId, entity.author, entity.rating,
                entity.text, entity.createdAt);
    }
}
