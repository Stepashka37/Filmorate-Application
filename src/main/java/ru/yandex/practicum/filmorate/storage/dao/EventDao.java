package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.module.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Repository
@Slf4j
public class EventDao {

    private static final String QUERY_FOR_EVENT = "insert into " +
            "EVENTS(TIMESTAMP, USER_ID, ENTITY_ID, EVENT_TYPE, OPERATION) VALUES (?, ?, ?, ?, ?)";
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
                resultSet.getString("EVENT_TYPE"), resultSet.getString("OPERATION"));
    }

    private void insertIntoDB(int userId, int entityId, String eventType, String operation) {
        long timestamp = Instant.now().toEpochMilli();
        jdbcTemplate.update(QUERY_FOR_EVENT, timestamp, userId, entityId, eventType, operation);
    }

    public void addLike(int userId, int filmId) {
        insertIntoDB(userId, filmId, "LIKE", "ADD");
    }

    public void addFriend(int userId, int userId1) {
        insertIntoDB(userId, userId1, "FRIEND", "ADD");
    }

    public void addReview(int userId, int reviewId) {
        insertIntoDB(userId, reviewId, "REVIEW", "ADD");
    }

    public void removeLike(int userId, int filmId) {
        insertIntoDB(userId, filmId, "LIKE", "REMOVE");
    }

    public void removeFriend(int userId, int userId1) {
        insertIntoDB(userId, userId1, "FRIEND", "REMOVE");
    }

    public void removeReview(int userId, int reviewId) {
        insertIntoDB(userId, reviewId, "REVIEW", "REMOVE");
    }

    public void updateReview(int userId, int reviewId) {
        insertIntoDB(userId, reviewId, "REVIEW", "UPDATE");
    }
}
