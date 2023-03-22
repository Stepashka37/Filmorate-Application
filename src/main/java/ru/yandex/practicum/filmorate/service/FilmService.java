package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.storage.FilmsStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmsStorage filmsStorage;

    public List<Film> getFilms() {
        return filmsStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmsStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmsStorage.updateFilm(film);
    }

    public void likeFilm(Long id, Long userId) {
        Film film = filmsStorage.getFilm(id);
        film.likeFilm(userId);
    }

    public void removeLike(Long id, Long userId) {
        Film film = filmsStorage.getFilm(id);
        film.removeLike(userId);

    }

    public List<Film> getMostLikedFilms(int count) {
        if (filmsStorage.getFilms().isEmpty()) {
            return new ArrayList<>();
        }
        if (filmsStorage.getFilms().size() <= count) {
            return new ArrayList<>(filmsStorage.getFilms());
        }
        List<Film> values = filmsStorage.getFilms().stream()
                .sorted((e1, e2) ->
                        Integer.compare(e1.getLikes().size(), e2.getLikes().size()))
                .collect(Collectors.toList());

        Collections.reverse(values);
        return values.subList(0, count);
    }

    public Film getFilm(Long id) {
        return filmsStorage.getFilm(id);
    }

    public void deleteFilm(Long id) {
        filmsStorage.deleteFilm(id);
    }

    public void deleteAllFilms() {
        filmsStorage.deleteAllFilms();
    }

}
