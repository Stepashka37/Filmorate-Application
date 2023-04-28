package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.module.Review;
import ru.yandex.practicum.filmorate.module.User;
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
        usersStorage.getUser(review.getUserId());
        filmsStorage.getFilm(review.getFilmId());
    }
}

