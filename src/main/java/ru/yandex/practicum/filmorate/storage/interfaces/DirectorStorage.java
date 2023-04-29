package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.Director;

import java.util.List;

public interface DirectorStorage {

    List<Director> findAll();

    Director findById(Integer id);

    Director addDirector(Director director) throws ValidationException;

    Director updateDirector(Director director) throws ValidationException;

    void deleteDirector(int id);

}
