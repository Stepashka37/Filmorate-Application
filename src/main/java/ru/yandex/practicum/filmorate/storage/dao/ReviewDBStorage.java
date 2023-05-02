package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.module.Review;
import ru.yandex.practicum.filmorate.storage.interfaces.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class ReviewDBStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review addReview(Review review) {
        String sql = "insert into REVIEW (CONTENT, POSITIVE, USER_ID, FILM_ID)" +
                "values (?, ?, ?, ?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setInt(3, review.getUserId());
            stmt.setInt(4, review.getFilmId());
            return stmt;
        }, keyHolder);

        int key = keyHolder.getKey().intValue();
        review.setReviewId(key);
        log.info("Добавили новое ревью с id{} ", key);
        return review;
    }

    @Override
    public Review updateReview(Review review) throws ReviewNotFoundException {
        String sql = "update REVIEW set CONTENT = ?, POSITIVE = ? where REVIEW_ID = ?";
        int rowsUpdated = jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());
        if (rowsUpdated == 0) {
            throw new ReviewNotFoundException("Ревью с id " + review.getReviewId() + " не найден");
        }
        log.info("Обновлено ревью с id {}", review.getReviewId());
        return getReview(review.getReviewId());
    }

    @Override
    public void deleteReview(int id) {
        String sql = "delete from REVIEW where REVIEW_ID = ?";
        jdbcTemplate.update(sql, id);
        log.info("Удалили ревью с id{} " + id);
    }

    @Override
    public Review getReview(int id) {
        String sql = "select * from REVIEW where REVIEW_ID = ?";
        log.info("Получили ревью с id{} " + id);
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs), id)
                .stream().findAny().orElseThrow(() -> new ReviewNotFoundException("Ревью с id " + id + " не найден"));
    }

    @Override
    public List<Review> getAllReviews(int count) {
        log.info("Получили все ревью");
        String sql = "select * from REVIEW order by " +
                "(select count(*) from REVIEW_LIKES where REVIEW_ID = REVIEW.REVIEW_ID and HELPFUL = true) - " +
                "(select count(*) from REVIEW_LIKES where REVIEW_ID = REVIEW.REVIEW_ID and HELPFUL = false) desc " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs), count);
    }

    @Override
    public List<Review> getFilmReviews(int filmId, int count) {
        String sql = "select * from REVIEW where FILM_ID = ? order by " +
                "(select count(*) from REVIEW_LIKES where REVIEW_ID = REVIEW.REVIEW_ID and HELPFUL = true) - " +
                "(select count(*) from REVIEW_LIKES where REVIEW_ID = REVIEW.REVIEW_ID and HELPFUL = false) desc " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{filmId, count}, (rs, rowNum) -> makeReview(rs));
    }

    @Override
    public void likeReview(int id, int userId) {
        String sqlAddLike = "merge into REVIEW_LIKES (review_id, user_id, helpful) values (?, ?, true)";
        jdbcTemplate.update(sqlAddLike, id, userId);
        log.info("Лайк ревью с ID: " + id);
    }

    @Override
    public void removeLike(int id, int userId) {
        String sqlRemoveLike = "delete from REVIEW_LIKES " +
                "where review_id = ? and user_id = ?";
        jdbcTemplate.update(sqlRemoveLike, id, userId);
        log.info("Удалили лайк ревью с ID: " + id);
    }

    @Override
    public void dislikeReview(int id, int userId) {
        String sqlAddLike = "merge into REVIEW_LIKES (review_id, user_id, helpful) values (?, ?, false)";
        jdbcTemplate.update(sqlAddLike, id, userId);
        log.info("Поcтавили дислайк ревью с ID: " + id);
    }

    @Override
    public void removeDislike(int id, int userId) {
        String sqlRemoveLike = "delete from REVIEW_LIKES " +
                "where review_id = ? and user_id = ?";
        jdbcTemplate.update(sqlRemoveLike, id, userId);
        log.info("Удалили дисклайк ревью с ID: " + id);
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        Review reviewBuilt = Review.builder()
                .reviewId(rs.getInt("REVIEW_ID"))
                .content(rs.getString("CONTENT"))
                .isPositive(rs.getBoolean("POSITIVE"))
                .userId(rs.getInt("USER_ID"))
                .filmId(rs.getInt("FILM_ID"))
                .build();

        Integer positive = jdbcTemplate.queryForObject("select count(*) from REVIEW_LIKES where review_id = ? AND helpful = true",
                new Object[]{reviewBuilt.getReviewId()}, Integer.class);

        Integer negative = jdbcTemplate.queryForObject("select count(*) from REVIEW_LIKES where review_id = ? AND helpful = false",
                new Object[]{reviewBuilt.getReviewId()}, Integer.class);

        reviewBuilt.setUseful(positive - negative);
        return reviewBuilt;
    }


}
