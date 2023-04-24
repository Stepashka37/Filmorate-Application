package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.module.Rating;
import ru.yandex.practicum.filmorate.storage.dao.RatingDao;

import java.util.List;

@Service
public class MpaService {
    private final RatingDao ratingDao;

    @Autowired
    public MpaService(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    public List<Rating> getRatings() {
        return ratingDao.findALl();
    }

    public Rating getRatingById(Integer id) {
        return ratingDao.findById(id);
    }
}
