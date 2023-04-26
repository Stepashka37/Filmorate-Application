package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.module.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static ru.yandex.practicum.filmorate.module.Event.EventType.*;
import static ru.yandex.practicum.filmorate.module.Event.Operation.*;

@Repository
@Slf4j
public class EventDao {

    private static final String QUERY_FOR_EVENT = "insert into " +
            "EVENTS(TIMESTAMP, USER_ID, ENTITY_ID, OPERATION, EVENT_TYPE) VALUES (?, ?, ?, ?, ?)";
    private final JdbcTemplate jdbcTemplate;

    public EventDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Event> getEvents(Integer userId) {
        String sqlQuery = "select* from EVENTS where USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToEvent, userId);
    }

    private Event mapRowToEvent(ResultSet resultSet, int rowNum) throws SQLException {
        return new Event(resultSet.getInt("EVENT_ID"), resultSet.getLong("TIMESTAMP"),
                resultSet.getInt("USER_ID"), resultSet.getInt("ENTITY_ID"),
                Event.Operation.valueOf(resultSet.getString("OPERATION")),
                Event.EventType.valueOf(resultSet.getString("EVENT_TYPE")));
    }

    private void insertIntoDB(int userId, int entityId, Event.Operation operation, Event.EventType eventType) {
        long timestamp = Instant.now().toEpochMilli();
        jdbcTemplate.update(QUERY_FOR_EVENT, timestamp, userId, entityId, operation.toString(), eventType.toString());
    }

    public void addLike(int userId, int filmId) {
        insertIntoDB(userId, filmId, ADD, LIKE);
    }

    public void addFriend(int userId, int userId1) {
        insertIntoDB(userId, userId1, ADD, FRIEND);
    }

    public void addReview(int userId, int reviewId) {
        insertIntoDB(userId, reviewId, ADD, REVIEW);
    }

    public void removeLike(int userId, int filmId) {
        insertIntoDB(userId, filmId, REMOVE, LIKE);
    }

    public void removeFriend(int userId, int userId1) {
        insertIntoDB(userId, userId1, REMOVE, FRIEND);
    }

    public void removeReview(int userId, int reviewId) {
        insertIntoDB(userId, reviewId, REMOVE, REVIEW);
    }

    public void updateReview(int userId, int reviewId) {
        insertIntoDB(userId, reviewId, UPDATE, REVIEW);
    }
}
