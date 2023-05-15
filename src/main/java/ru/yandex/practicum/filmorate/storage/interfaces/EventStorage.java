package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.module.Event;

import java.util.List;

public interface EventStorage {
    List<Event> getEvents(Integer userId);

    void addScore(int userId, int filmId);

    void addFriend(int userId, int userId1);

    void addReview(int userId, int reviewId);

    void removeScore(int userId, int filmId);

    void removeFriend(int userId, int userId1);

    void removeReview(int userId, int reviewId);

    void updateReview(int userId, int reviewId);
}
