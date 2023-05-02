package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.module.Director;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DirectorDao implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> findAll() {
        String sql = "select * from directors";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs));
    }

    @Override
    public Director findById(Integer id) {
        String sql = "select * from directors where DIRECTOR_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs), id)
                .stream().findAny().orElseThrow(() -> new DirectorNotFoundException("Режиссер с id " + id + " не найден"));
    }

    @Override
    public Director addDirector(Director director) {

        String sql = "insert into directors (NAME)" +
                "values (?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);

        int key = keyHolder.getKey().intValue();

        return jdbcTemplate.query("select * from directors where director_id = ?", (rs, rowNum) -> makeDirector(rs), key)
                .stream().findAny().orElse(null);

    }

    @Override
    public Director updateDirector(Director director) {

        String sql = "update DIRECTORS set " +
                "NAME = ? " +
                "WHERE DIRECTOR_ID = ?";

        int checkNum = jdbcTemplate.update(sql,
                director.getName(),
                director.getId());

        if (checkNum == 0) {
            throw new DirectorNotFoundException("Режиссер с id" + director.getId() + " не найден");
        }

        return jdbcTemplate.query("select * from directors where director_id = ?", (rs, rowNum) -> makeDirector(rs), director.getId())
                .stream().findAny().orElse(null);
    }

    @Override
    public void deleteDirector(int id) {
        String sqlDeleteLink = "delete from FILM_DIRECTORS WHERE director_id = ?";
        jdbcTemplate.update(sqlDeleteLink, id);
        String sqlDeleteDirector = "delete from directors where director_id = ? ";
        jdbcTemplate.update(sqlDeleteDirector, id);
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        return Director.builder()
                .id(rs.getInt("director_id"))
                .name(rs.getString("name"))
                .build();
    }
}
