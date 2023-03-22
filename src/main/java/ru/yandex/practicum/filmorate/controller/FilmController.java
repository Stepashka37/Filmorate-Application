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

    private FilmService filmService;

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
        Film addedFilm = filmService.addFilm(film);
        log.info("Добавили фильм с id{}", addedFilm.getId());
        return addedFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film updatedFilm = filmService.updateFilm(film);

        log.info("Обновили фильм с id{}", updatedFilm.getId());
        return updatedFilm;
    }

    @DeleteMapping()
    public void deleteAllFilms() {
        filmService.deleteAllFilms();
        log.info("Все фильмы были удален");

    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.likeFilm(id, userId);
        log.info("Пользователь с id{}", userId + " поставил лайк фильму с id" + id);

    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        log.info("Пользователь с id{}", userId + " убрал лайк фильму с id" + id);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        Film film = filmService.getFilm(id);
        log.info("Получили фильм с id{}", id);
        return film;
    }

    @GetMapping(value = {"/popular?count={count}", "/popular"})
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Min(1) Integer count) {
        /*if (count == null) {
            count = 10;
        }
        if (count < 0) {
            throw new ValidationException("count не может быть меньше 0");
        }*/
        List<Film> popularFilms = filmService.getMostLikedFilms(count);
        log.info("Получили список из " + count + " наиболее популярных фильмов");
        return popularFilms;
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        log.info("Удалили фильм с id{}", id);
    }


}
