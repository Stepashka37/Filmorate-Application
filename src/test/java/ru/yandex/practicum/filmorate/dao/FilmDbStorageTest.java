package ru.yandex.practicum.filmorate.dao;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.module.Rating;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @BeforeEach
    public void setData() {
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

        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);
        filmDbStorage.addFilm(film3);

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

        assertEquals(3, films.size());
    }

    @Test
    public void addfilmTest() {
        Film film4 = Film.builder()
                .name("film4")
                .description("film4Description")
                .releaseDate(LocalDate.of(1998, 06, 03))
                .duration(140)
                .mpa(new Rating(1, "Drama"))
                .genres(new HashSet<>())
                .build();
        Film film1Created = filmDbStorage.addFilm(film4);

        assertEquals(4, film1Created.getId());
        assertEquals(4, filmDbStorage.getFilms().size());

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
        assertEquals(3, filmDbStorage.getFilms().size());
    }

    @Test
    public void deleteFilmTest() {
        filmDbStorage.deleteFilm(1);

        assertEquals(2, filmDbStorage.getFilms().size());
    }

    @Test
    public void deleteFilmsTest() {
        filmDbStorage.deleteAllFilms();

        assertEquals(0, filmDbStorage.getFilms().size());
    }

    @Test
    public void addLikeTest() {
        filmDbStorage.addLike(1, 2);

        assertEquals(1, filmDbStorage.getFilm(1).getLikes().size());
    }

    @Test
    public void removeLikeTest() {

        filmDbStorage.addLike(1, 2);

        assertEquals(1, filmDbStorage.getFilm(1).getLikes().size());

        filmDbStorage.removeLike(1, 2);

        assertEquals(0, filmDbStorage.getFilm(1).getLikes().size());
    }

    @Test
    public void getPopularFilms() {
        filmDbStorage.addLike(1, 2);
        List<Film> popularFilms = filmDbStorage.getPopularFilms(1);
        Film popularFilm = filmDbStorage.getFilm(1);
        assertEquals(popularFilm, popularFilms.get(0));

        popularFilms = filmDbStorage.getPopularFilms(3);
        assertEquals(3, popularFilms.size());
    }

}
