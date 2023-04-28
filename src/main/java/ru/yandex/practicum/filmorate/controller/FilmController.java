package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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

    @GetMapping(value = {"/popular?count={count}", "/popular"})
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Min(1) Integer count) {
        List<Film> popularFilms = filmService.getMostLikedFilms(count);
        log.info("Получили список из " + popularFilms.size() + " наиболее популярных фильмов");
        return popularFilms;
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        filmService.deleteFilm(id);
        log.info("Удалили фильм с id{}", id);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam String query, @RequestParam List<String> by) {
        List<Film> films = filmService.searchFilms(query, by);
        log.info("Нашли фильмы по запросу {}", query);
        return films;
    }



    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorsFilms(@PathVariable Integer directorId,
                                        @RequestParam String sortBy) {
        return filmService.getDirectorsFilms(directorId, sortBy);
    }

}
