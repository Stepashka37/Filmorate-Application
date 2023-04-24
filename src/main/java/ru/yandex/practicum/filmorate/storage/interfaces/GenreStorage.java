package ru.yandex.practicum.filmorate.storage.interfaces;


import ru.yandex.practicum.filmorate.module.Genre;

import java.util.List;


public interface GenreStorage {

    List<Genre> findAll();

    Genre findById(Integer id);
}
