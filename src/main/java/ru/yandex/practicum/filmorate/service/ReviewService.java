package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.Review;
import ru.yandex.practicum.filmorate.storage.interfaces.ReviewStorage;

import java.util.List;

@Service
public class ReviewService {

    ReviewStorage reviewStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }


    public Review addReview(Review review) {
        validateReview(review);
        return reviewStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        validateReview(review);
        return reviewStorage.updateReview(review);
    }

    public void deleteReview(int id) {
        reviewStorage.deleteReview(id);
    }

    public Review getReview(int id) {
        return reviewStorage.getReview(id);
    }

    public void likeReview(int id, int userId) {
        reviewStorage.likeReview(id, userId);
    }

    public void removeLike(int id, int userId) {
        reviewStorage.removeLike(id, userId);
    }

    public void dislikeReview(int id, int userId) {
        reviewStorage.dislikeReview(id, userId);
    }

    public void removeDislike(int id, int userId) {
        reviewStorage.removeDislike(id, userId);
    }

    public List<Review> getReviews(int filmId, int count) {
        if (filmId == 0) {
            return reviewStorage.getAllReviews(count);
        } else {
            return reviewStorage.getFilmReviews(filmId, count);
        }
    }

    private void validateReview(Review review) {
        if (review.getContent() == null || review.getContent().isBlank()) {
            throw new ValidationException("Содерживое не должно быть пустым");
        }
    }
}
