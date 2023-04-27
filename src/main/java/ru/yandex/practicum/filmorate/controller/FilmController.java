package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        List<Film> films = filmService.getFilms();
        log.info("Получили список всех фильмов");
        return films;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        Film addFilm = filmService.addFilm(film);
        log.info("Добавили фильм с id{}", addFilm.getId());
        //return addedFilm;
        return addFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film updateFilm = filmService.updateFilm(film);

        log.info("Обновили фильм с id{}", film.getId());
        return updateFilm;
    }

    @DeleteMapping()
    public void deleteAllFilms() {
        filmService.deleteAllFilms();
        log.info("Все фильмы были удален");

    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.likeFilm(id, userId);
        log.info("Пользователь с id{}", userId + " поставил лайк фильму с id" + id);

    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
        log.info("Пользователь с id{}", userId + " убрал лайк фильму с id" + id);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        Film film = filmService.getFilm(id);
        log.info("Получили фильм с id{}", id);
        return film;
    }


    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        filmService.deleteFilm(id);
        log.info("Удалили фильм с id{}", id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularByGenreAndYear(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "0") int genreId,
            @RequestParam(defaultValue = "0") int year
    ) {
        log.debug("Popular films requested");
        return filmService.getPopularByGenreAndYear(year, genreId, count);
    }

}
