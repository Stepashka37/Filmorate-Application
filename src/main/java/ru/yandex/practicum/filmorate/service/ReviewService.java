package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.module.Review;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UsersStorage;

import java.util.List;

@Service
public class ReviewService {

    ReviewStorage reviewStorage;
    UsersStorage usersStorage;
    FilmsStorage filmsStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, @Qualifier("userDbStorage") UsersStorage usersStorage,
                         @Qualifier("filmDbStorage") FilmsStorage filmsStorage) {
        this.reviewStorage = reviewStorage;
        this.usersStorage = usersStorage;
        this.filmsStorage = filmsStorage;
    }


    public Review addReview(Review review) {
        validateReview(review);
        return reviewStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        validateReview(review);
        return reviewStorage.updateReview(review);
    }

    public void deleteReview(int reviewId) {
        reviewStorage.deleteReview(reviewId);
    }

    public Review getReview(int reviewId) {
        return reviewStorage.getReview(reviewId);
    }

    public void likeReview(int reviewId, int userId) {
        validateLike(reviewId, userId);
        reviewStorage.likeReview(reviewId, userId);
    }

    public void removeLike(int id, int userId) {
        reviewStorage.removeLike(id, userId);
    }

    public void dislikeReview(int reviewId, int userId) {
        validateLike(reviewId, userId);
        reviewStorage.dislikeReview(reviewId, userId);
    }

    public void removeDislike(int reviewId, int userId) {
        reviewStorage.removeDislike(reviewId, userId);
    }

    public List<Review> getReviews(int filmId, int count) {
        if (filmId == 0 || filmsStorage.getFilm(filmId) == null) {
            return reviewStorage.getAllReviews(count);
        } else {
            return reviewStorage.getFilmReviews(filmId, count);
        }
    }

    private void validateLike(int reviewId, int userId) {
        reviewStorage.getReview(reviewId);
        usersStorage.getUser(userId);
    }

    private void validateReview(Review review) {
        usersStorage.getUser(review.getUserId());
        filmsStorage.getFilm(review.getFilmId());
    }
}

