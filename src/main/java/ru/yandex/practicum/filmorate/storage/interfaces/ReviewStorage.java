package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.module.Review;

import java.util.List;

public interface ReviewStorage {

    public Review addReview(Review review);

    public Review updateReview(Review review);

    public void deleteReview(int id);

    public Review getReview(int id);

    void likeReview(int id, int userId);

    void removeLike(int id, int userId);

    void dislikeReview(int id, int userId);

    void removeDislike(int id, int userId);

    List<Review> getAllReviews(int count);

    List<Review> getFilmReviews(int filmId, int count);
}
