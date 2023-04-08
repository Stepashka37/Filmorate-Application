package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.module.Rating;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RatingDao implements RatingMpa {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Rating> findALl() {
        String sql = "select * from RATING_MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    @Override
    public Rating findById(Integer id) {
        String sql = "select * from RATING_MPA where RATING_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs), id)
                .stream().findAny().orElseThrow(() -> new MpaNotFoundException("Рейтинг с id " + id + " не найден"));
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        Rating rating = Rating.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("name")).build();
        return rating;
    }


}
