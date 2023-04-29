package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsStorage;

import java.util.*;
import java.util.stream.Collectors;


public class InMemoryFilmStorage implements FilmsStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int genId = 0;


    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с id " + id + " не найден");
        }
        return films.get(id);
    }


    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }


    @Override
    public Film addFilm(Film film) {
        ++genId;
        film.setId(genId);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(film.getId(), film);
        return films.get(film.getId());
    }


    @Override
    public void deleteAllFilms() {
        films.clear();
        genId = 0;
    }

    @Override
    public void deleteFilm(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с id " + id + " не найден");
        }
        films.remove(id);
    }

    @Override
    public void addLike(int filmId, int userId) {
        films.get(filmId).likeFilm(userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        films.get(filmId).removeLike(userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        if (films.values().isEmpty()) {
            return new ArrayList<>();
        }
        if (films.values().size() <= count) {
            return new ArrayList<>(films.values());
        }
        List<Film> values = films.values().stream()
                .sorted((e1, e2) ->
                        Integer.compare(e1.getLikes().size(), e2.getLikes().size()))
                .collect(Collectors.toList());

        Collections.reverse(values);
        return values.subList(0, count);
    }

    @Override
    public List<Film> getPopularByYear(int year, int count) {
        return new ArrayList<>();
    }

    @Override
    public List<Film> getPopularByGenreAndYear(int year, int genreId, int count) {
        return new ArrayList<>();
    }

    @Override
    public List<Film> getPopularByGenre(int genreId, int count) {
        return new ArrayList<>();
    }

}

