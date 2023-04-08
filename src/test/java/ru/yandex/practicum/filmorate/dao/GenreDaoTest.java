package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.module.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTest {
    private final GenreDao genreDao;


    @Test
    public void getGenreTest() {

        Genre genre = genreDao.findById(1);

        assertNotNull(genre);
        assertEquals(1, genre.getId());
    }

    @Test
    public void getGenresTest() {
        List<Genre> genres = genreDao.findAll();
        assertEquals(6, genres.size());
    }
}
