package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.module.Director;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.module.Genre;
import ru.yandex.practicum.filmorate.module.Rating;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@Slf4j
public class FilmDbStorage implements FilmsStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilm(int id) {
        String sql = "select *  from film where  film_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id)
                .stream().findAny().orElseThrow(() -> new FilmNotFoundException("Фильм с id " + id + " не найден"));

    }

    @Override
    public List<Film> getFilms() {
        String sql = "select * from film ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film addFilm(Film film) {

        String sql = "insert into film (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)" +
                "values (?, ?, ?, ?, ?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        int key = keyHolder.getKey().intValue();
        film.setId(key);
        if (film.getGenres() != null) {
            addGenres(film);
        }

        if (film.getDirectors() != null) {
            addDirectors(film);
        }
        return jdbcTemplate.query("select * from film where film_id = ?", (rs, rowNum) -> makeFilm(rs), key)
                .stream().findAny().orElse(null);
    }

    @Override
    public Film updateFilm(Film film) {

        String sql = "update FILM set " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? " +
                "WHERE FILM_ID = ?";

        int checkNum = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (checkNum == 0) {
            throw new FilmNotFoundException("Фильм с  id \"" + film.getId() + "\" не найден.");
        }

        if (film.getGenres() != null) {
            String sqlDeleteAll = "delete from FILM_GENRES WHERE film_id = ?";
            jdbcTemplate.update(sqlDeleteAll, film.getId());
            addGenres(film);
        }

        if (film.getDirectors() != null) {
            String sqlDeleteAll = "delete from FILM_DIRECTORS WHERE film_id = ?";
            jdbcTemplate.update(sqlDeleteAll, film.getId());
            addDirectors(film);
        } else {
            String sqlDeleteAll = "delete from FILM_DIRECTORS WHERE film_id = ?";
            jdbcTemplate.update(sqlDeleteAll, film.getId());
        }

        return jdbcTemplate.query("select * from film where film_id = ?", (rs, rowNum) -> makeFilm(rs), film.getId())
                .stream().findAny().orElse(null);
    }

    @Override
    public void deleteAllFilms() {
        String sql = "SET REFERENTIAL_INTEGRITY FALSE;" +
                "TRUNCATE TABLE   FILM; " +
                "TRUNCATE TABLE FILM_GENRES; " +
                "TRUNCATE TABLE FILM_LIKES; " +
                "SET REFERENTIAL_INTEGRITY FALSE;" +
                "alter table FILM alter column FILM_ID restart with 1";

        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteFilm(int id) {
        String sqlDeleteFIlm = "delete from FILM where FILM_ID = ? ";
        String sqlDeleteFilmGenres = "delete from FILM_GENRES where FILM_ID = ?";
        String sqlDeleteFilmLikes = "delete from FILM_LIKES where FILM_ID = ?";
        jdbcTemplate.update(sqlDeleteFilmGenres, id);
        jdbcTemplate.update(sqlDeleteFilmLikes, id);
        jdbcTemplate.update(sqlDeleteFIlm, id);
    }

    @Override
    public void addScore(int filmId, int userId, int score) {
        String sqlAddScore = "merge into FILM_LIKES (FILM_ID, USER_ID, SCORE) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlAddScore, filmId, userId, score);
    }

    @Override
    public void removeScore(int filmId, int userId) {
        String sqlRemoveScore = "delete from FILM_LIKES " +
                "where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlRemoveScore, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlGetPopularFilms = "select  F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATING_ID from FILM AS F " +
                "left join FILM_LIKES AS FL on F.FILM_ID = FL.FILM_ID " +
                "group by F.FILM_ID " +
                "order by count(FL.USER_ID) DESC " +
                "limit ?";
        return jdbcTemplate.query(sqlGetPopularFilms, (rs, rowNum) -> makeFilm(rs), count);
    }

    @Override
    public List<Film> getFilmByDirectorQuery(String query) {
        String sql = "select  F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATING_ID from FILM AS F " +
                "left join FILM_DIRECTORS AS FD on F.FILM_ID = FD.FILM_ID " +
                "left join DIRECTORS AS D on FD.DIRECTOR_ID = D.DIRECTOR_ID " +
                "left  join FILM_LIKES FL on F.FILM_ID = FL.FILM_ID " +
                "where D.NAME LIKE ? " +
                "group by F.FILM_ID " +
                "order by count(FL.USER_ID) DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), "%" + query + "%");
    }

    @Override
    public List<Film> getFilmByFilmQuery(String query) {
        String sql = "select F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATING_ID from FILM as F " +
                "left join FILM_LIKES AS FL on F.FILM_ID = FL.FILM_ID " +
                "where NAME LIKE ?" +
                "group by F.FILM_ID " +
                "order by count(FL.USER_ID) DESC ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), "%" + query + "%");
    }


    @Override
    public List<Film> recommendFilms(Integer userId) {
        String thisUserLikes = "select fl.film_id" +
                " from film_likes as fl " +
                " where fl.user_id = " + userId;
        String usersWithSameLikes = "select user_id" +
                " from film_likes" +
                " where film_id in (" + thisUserLikes + ") and user_id != " + userId +
                " group by user_id" +
                " order by count(user_id) desc";
        String recommendedFilmsIds = "select film_id" +
                " from film_likes as fl" +
                " where user_id in (" + usersWithSameLikes +
                ") and film_id not in (" + thisUserLikes + ")";
        String findFilms = "select * from film where  film_id in (" + recommendedFilmsIds + ")";
        return jdbcTemplate.query(findFilms, (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film filmBuilt = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .build();
        String sqlQueryForRating = "select name from RATING_MPA where rating_id = " + rs.getInt("rating_id");
        String sqlQueryForGenres = "select G.GENRE_ID, NAME from GENRE G " +
                "join FILM_GENRES FG ON FG.GENRE_ID = G.GENRE_ID WHERE FG.FILM_ID = " + filmBuilt.getId();
        String sqlQueryForDirectors = "select D.DIRECTOR_ID, D.NAME from DIRECTORS as D " +
                "join FILM_DIRECTORS as FD ON FD.DIRECTOR_ID = D.DIRECTOR_ID WHERE FD.FILM_ID = " + filmBuilt.getId();

        String sqlQueryForScore = "select AVG(SCORE) from film_likes where film_id = ?";

        filmBuilt.setMpa(new Rating(rs.getInt("rating_id"), jdbcTemplate.queryForObject(sqlQueryForRating, String.class)));

        List<Genre> genres = jdbcTemplate.query(
                sqlQueryForGenres,
                (rss, rowNum) ->
                        new Genre(
                                rss.getInt("genre_id"),
                                rss.getString("name")
                        )
        );
        filmBuilt.setGenres(new HashSet<>(genres));

        List<Director> directors = jdbcTemplate.query(
                sqlQueryForDirectors,
                (rss, rowNum) ->
                        new Director(
                                rss.getInt("director_id"),
                                rss.getString("name")
                        )
        );
        filmBuilt.setDirectors(new HashSet<>(directors));
        Double score = jdbcTemplate.queryForObject(sqlQueryForScore, Double.class, filmBuilt.getId());
        if (score != null) filmBuilt.setScore(score);
        return filmBuilt;
    }

    @Override
    public List<Film> getPopularByGenreAndYear(int year, int genreId, int count) {
        return jdbcTemplate.query(
                "SELECT f.* " +
                        "FROM film AS f " +
                        "LEFT JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                        "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                        "WHERE YEAR(f.release_date) = ? AND fg.genre_id = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY COUNT(fl.user_id) " +
                        "LIMIT ?;", (rs, rowNum) -> makeFilm(rs), year, genreId, count);
    }

    @Override
    public List<Film> getPopularByYear(int year, int count) {
        return jdbcTemplate.query(
                "SELECT f.*, COUNT(fl.user_id) AS rate " +
                        "FROM film AS f " +
                        "LEFT JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                        "WHERE YEAR(f.release_date) = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY rate " +
                        "LIMIT ?;", (rs, rowNum) -> makeFilm(rs), year, count);
    }

    @Override
    public List<Film> getPopularByGenre(int genreId, int count) {
        return jdbcTemplate.query(
                "SELECT f.*, COUNT(fl.user_id) AS rate " +
                        "FROM film AS f " +
                        "LEFT JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                        "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                        "WHERE fg.genre_id = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY rate " +
                        "LIMIT ?;", (rs, rowNum) -> makeFilm(rs), genreId, count);
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        log.debug("Common films are: ");
        String query = "SELECT f.* FROM film f " +
                "WHERE f.film_id IN (SELECT l1.film_id FROM film_likes l1 WHERE l1.user_id = ?) " +
                "AND f.film_id IN (SELECT l2.film_id FROM film_likes l2 WHERE l2.user_id = ?)";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeFilm(rs), userId, friendId);
    }

    public List<Film> getDirectorsFilms(int directorId, String sortBy) {
        final String sqlQueryForCheck = "select COUNT(d.director_id) " +
                "from directors as d " +
                "where d.director_id = ? ";
        Integer count = jdbcTemplate.queryForObject(sqlQueryForCheck, Integer.class, directorId);
        if (count == null || count == 0) {
            throw new DirectorNotFoundException("Режиссер с  id \"" + directorId + "\" не найден.");
        }

        String sql = "";
        if (sortBy.equals("year")) {
            sql = "select * from FILM as F " +
                    "join FILM_DIRECTORS as FD ON F.FILM_ID = FD.FILM_ID WHERE FD.DIRECTOR_ID = " + directorId +
                    " order by f.release_date";

        } else if (sortBy.equals("likes")) {
            sql = "select F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATING_ID " +
                    " from film_directors as fd " +
                    " join film as f on fd.film_id = f.film_id " +
                    " left join film_likes fl on f.film_id =  fl.film_id " +
                    "where fd.director_id = " + directorId +
                    " group by f.film_id " +
                    "order by count(f.film_id) desc";

        }

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    private void addGenres(Film film) {
        String sqlQueryForGenres = "merge into film_genres (film_id, genre_id) values (?, ?)";
        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(sqlQueryForGenres, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, film.getId());
                preparedStatement.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    private void addDirectors(Film film) {
        String sqlQueryForGenres = "merge into film_directors (film_id, director_id) values (?, ?)";
        List<Director> directors = new ArrayList<>(film.getDirectors());
        jdbcTemplate.batchUpdate(sqlQueryForGenres, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, film.getId());
                preparedStatement.setInt(2, directors.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return directors.size();
            }
        });
    }
}
