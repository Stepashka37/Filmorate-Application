package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.module.Rating;
import ru.yandex.practicum.filmorate.storage.dao.RatingDao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDaoTest {
    private final RatingDao ratingDao;

    @Test
    public void getRatingTest() {
        Rating rating = ratingDao.findById(1);

        assertNotNull(rating);
        assertEquals(1, rating.getId());
    }

    @Test
    public void getRatingsTest() {
        List<Rating> ratings = ratingDao.findALl();
        assertEquals(5, ratings.size());
    }
}
