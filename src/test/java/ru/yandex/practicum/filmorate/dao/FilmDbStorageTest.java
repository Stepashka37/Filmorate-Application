package ru.yandex.practicum.filmorate.dao;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.yandex.practicum.filmorate.module.Director;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.module.Rating;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.storage.dao.DirectorDao;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final DirectorDao directorDao;

    @BeforeEach
    public void setData() {
        Director director = Director.builder()
                .name("director1")
                .build();

        directorDao.addDirector(director);

        Set<Director> filmDirectors = new HashSet<>();
        filmDirectors.add(directorDao.findById(1));

        Film film1 = Film.builder()
                .name("film1")
                .description("film1Description")
                .releaseDate(LocalDate.of(2012, 03, 04))
                .duration(120)
                .mpa(new Rating(1, "Drama"))
                .genres(new HashSet<>())
                .build();

        Film film2 = Film.builder()
                .name("film2")
                .description("film2Description")
                .releaseDate(LocalDate.of(2013, 03, 04))
                .duration(120)
                .mpa(new Rating(1, "Drama"))
                .genres(new HashSet<>())
                .build();

        Film film3 = Film.builder()
                .name("film3")
                .description("film3Description")
                .releaseDate(LocalDate.of(2014, 03, 04))
                .duration(120)
                .mpa(new Rating(1, "Drama"))
                .genres(new HashSet<>())
                .build();

        Film film4 = Film.builder()
                .name("film4")
                .description("film4Description")
                .releaseDate(LocalDate.of(2018, 03, 04))
                .duration(120)
                .mpa(new Rating(1, "Drama"))
                .genres(new HashSet<>())
                .directors(filmDirectors)
                .build();

        Film film5 = Film.builder()
                .name("film5")
                .description("film5Description")
                .releaseDate(LocalDate.of(2020, 03, 04))
                .duration(120)
                .mpa(new Rating(1, "Drama"))
                .genres(new HashSet<>())
                .directors(filmDirectors)
                .build();

        Film film6 = Film.builder()
                .name("film6")
                .description("film6Description")
                .releaseDate(LocalDate.of(2021, 03, 04))
                .duration(120)
                .mpa(new Rating(1, "Drama"))
                .genres(new HashSet<>())
                .directors(filmDirectors)
                .build();

        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);
        filmDbStorage.addFilm(film3);
        filmDbStorage.addFilm(film4);
        filmDbStorage.addFilm(film5);
        filmDbStorage.addFilm(film6);

        User user1 = User.builder()
                .email("yandex1@yandex.ru")
                .login("user1")
                .name("user1")
                .birthday(LocalDate.of(1997, 06, 03))
                .build();

        User user2 = User.builder()
                .email("yandex2@yandex.ru")
                .login("user2")
                .name("user2")
                .birthday(LocalDate.of(1996, 06, 03))
                .build();

        User user3 = User.builder()
                .email("yandex3@yandex.ru")
                .login("user3")
                .name("user3")
                .birthday(LocalDate.of(1995, 06, 03))
                .build();

        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);
        userDbStorage.addUser(user3);

    }

    @AfterEach
    public void cleanAndResetId() {
        filmDbStorage.deleteAllFilms();
        userDbStorage.deleteAllUsers();
    }

    @Test
    public void getFilmTest() {

        Film film = filmDbStorage.getFilm(1);

        assertNotNull(film);
        assertEquals(1, film.getId());
    }

    @Test
    public void getFilmsTest() {
        List<Film> films = filmDbStorage.getFilms();

        assertEquals(6, films.size());
    }

    @Test
    public void addFilmTest() {
        Film film4 = Film.builder()
                .name("film4")
                .description("film4Description")
                .releaseDate(LocalDate.of(1998, 06, 03))
                .duration(140)
                .mpa(new Rating(1, "Drama"))
                .genres(new HashSet<>())
                .build();
        Film film1Created = filmDbStorage.addFilm(film4);

        assertEquals(7, film1Created.getId());
        assertEquals(7, filmDbStorage.getFilms().size());

    }

    @Test
    public void updateFilmTest() {

        Film filmUpd = Film.builder()
                .id(1)
                .name("film1Upd")
                .description("film1UpdDescription")
                .releaseDate(LocalDate.of(1999, 06, 03))
                .mpa(new Rating(1, "Drama"))
                .genres(new HashSet<>())
                .build();
        Film filmUpdated = filmDbStorage.updateFilm(filmUpd);

        assertEquals("film1Upd", filmUpdated.getName());
        assertEquals(6, filmDbStorage.getFilms().size());
    }

    @Test
    public void deleteFilmTest() {
        filmDbStorage.deleteFilm(1);

        assertEquals(5, filmDbStorage.getFilms().size());
    }

    @Test
    public void deleteFilmsTest() {
        filmDbStorage.deleteAllFilms();

        assertEquals(0, filmDbStorage.getFilms().size());
    }

    @Test
    public void addScoreTest() {
        filmDbStorage.addScore(1, 2, 6);
        Film film = filmDbStorage.getFilm(1);
        assertEquals(6, film.getScore());
    }

    @Test
    public void addScoreAvgTest() {
        filmDbStorage.addScore(1, 2, 10);
        filmDbStorage.addScore(1, 1, 2);
        Film film = filmDbStorage.getFilm(1);
        assertEquals(6, film.getScore());
    }

    @Test
    public void addWrongScoreTest() {
        assertThrows(DataIntegrityViolationException.class, () -> filmDbStorage.addScore(1, 2, 1000));
        assertThrows(DataIntegrityViolationException.class, () -> filmDbStorage.addScore(1, 1, -222));
        assertThrows(DataIntegrityViolationException.class, () -> filmDbStorage.addScore(1, 1, 11));
        assertDoesNotThrow(() -> filmDbStorage.addScore(1, 1, 10));
        assertDoesNotThrow(() -> filmDbStorage.addScore(1, 1, 1));
    }

    @Test
    public void removeScoreTest() {
        filmDbStorage.addScore(1, 2, 9);
        assertEquals(9, filmDbStorage.getFilm(1).getScore());
        filmDbStorage.removeScore(1, 2);
        assertEquals(0.0, filmDbStorage.getFilm(1).getScore());
    }

    @Test
    public void getPopularFilms() {
        filmDbStorage.addScore(1, 2, 7);
        List<Film> popularFilms = filmDbStorage.getPopularFilms(1);
        Film popularFilm = filmDbStorage.getFilm(1);
        assertEquals(popularFilm, popularFilms.get(0));

        popularFilms = filmDbStorage.getPopularFilms(3);
        assertEquals(3, popularFilms.size());
    }

    @Test
    public void getDirectorsFilmsSortByScoreSuccess() {
        filmDbStorage.addScore(4, 2, 10);
        filmDbStorage.addScore(4, 1, 3);
        filmDbStorage.addScore(5, 2, 8);
        filmDbStorage.addScore(5, 1, 6);
        final List<Film> directorsFilms = filmDbStorage.getDirectorsFilms(1, "score");
        final Film film4 = filmDbStorage.getFilm(4);
        final Film film5 = filmDbStorage.getFilm(5);
        final Film film6 = filmDbStorage.getFilm(6);
        assertEquals(film5, directorsFilms.get(0));
        assertEquals(film4, directorsFilms.get(1));
        assertEquals(film6, directorsFilms.get(2));
        assertEquals(3, directorsFilms.size());
    }

    @Test
    public void getRecommendation() {
        User user4 = User.builder()
                .email("yandex4@yandex.ru")
                .login("user4")
                .name("user4")
                .birthday(LocalDate.of(1995, 06, 03))
                .build();
        userDbStorage.addUser(user4);

        filmDbStorage.addScore(1, 1, 7);
        filmDbStorage.addScore(1, 2, 9);
        filmDbStorage.addScore(1, 3, 5);
        filmDbStorage.addScore(1, 4, 7);

        filmDbStorage.addScore(2, 1, 2);
        filmDbStorage.addScore(2, 2, 1);
        filmDbStorage.addScore(2, 3, 6);
        filmDbStorage.addScore(2, 4, 3);

        // user1 не оценивал film3 и должен получить его по рекомендации user2
        // так же рекомендован user4, но дублирования не должно быть
        filmDbStorage.addScore(3, 2, 6);
        filmDbStorage.addScore(3, 3, 4);
        filmDbStorage.addScore(3, 4, 7);

        // user1 не оценивал film4 и должен получить его по рекомендации user4
        filmDbStorage.addScore(4, 2, 3);
        filmDbStorage.addScore(4, 3, 8);
        filmDbStorage.addScore(4, 4, 9);

        List<Film> recommendedFilms = filmDbStorage.recommendFilms(1);
        List<Film> checkList = new ArrayList<>();
        checkList.add(filmDbStorage.getFilm(3));
        checkList.add(filmDbStorage.getFilm(4));
        assertEquals(recommendedFilms, checkList, "Должны быть рекомендованы фильмы 3 и 4.");
    }

}
