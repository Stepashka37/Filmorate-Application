package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
@Tag(name = "Films", description = "Requests for films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    @Operation(summary = "Get list of all films")
    public List<Film> getFilms() {
        List<Film> films = filmService.getFilms();
        log.info("Получили список всех фильмов");
        return films;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add new film to database")
    public Film addFilm(@Valid @RequestBody Film film) {
        Film addFilm = filmService.addFilm(film);
        log.info("Добавили фильм с id{}", addFilm.getId());
        return addFilm;
    }

    @PutMapping
    @Operation(summary = "Update film in the database")
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film updateFilm = filmService.updateFilm(film);
        log.info("Обновили фильм с id{}", film.getId());
        return updateFilm;
    }

    @DeleteMapping()
    @Operation(summary = "Delete all films")
    public void deleteAllFilms() {
        filmService.deleteAllFilms();
        log.info("Все фильмы были удален");
    }

    @PutMapping("/{id}/score/{userId}")
    @Operation(summary = "Add user score to film by id")
    public void scoreFilm(@PathVariable int id, @PathVariable int userId, @RequestParam
    @Min(value = 1, message = "Минимальный рейтинг, который можно поставить фильму: 1")
    @Max(value = 10, message = "Максимальный рейтинг, который можно поставить фильму: 10") int score) {
        filmService.scoreFilm(id, userId, score);
        log.info("Пользователь с id{} поставил оценку {} фильму с id{}", userId, score, id);
    }

    @DeleteMapping("/{id}/score/{userId}")
    @Operation(summary = "Remove user score from film by id")
    public void removeScore(@PathVariable int id, @PathVariable int userId) {
        filmService.removeScore(id, userId);
        log.info("Пользователь с id{} убрал оценку фильму с id{}", userId, id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get film by id")
    public Film getFilm(@PathVariable int id) {
        Film film = filmService.getFilm(id);
        log.info("Получили фильм с id{}", id);
        return film;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete film by id")
    public void deleteFilm(@PathVariable int id) {
        filmService.deleteFilm(id);
        log.info("Удалили фильм с id{}", id);
    }

    @GetMapping("/common")
    @Operation(summary = "Get common films between 2 users")
    public List<Film> getCommonFilms(@RequestParam int userId, @RequestParam int friendId) {
        log.debug("try to get common films");
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for the film by query")
    public List<Film> searchFilms(@RequestParam String query, @RequestParam List<String> by) {
        List<Film> films = filmService.searchFilms(query, by);
        log.info("Нашли фильмы по запросу {}", query);
        return films;
    }

    @GetMapping("/popular")
    @Operation(summary = "Get list of popular films by genre and year")
    public List<Film> getPopularByGenreAndYear(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "0") int genreId,
            @RequestParam(defaultValue = "0") int year
    ) {
        log.debug("Popular films requested");
        return filmService.getPopularByGenreAndYear(year, genreId, count);
    }

    @GetMapping("/director/{directorId}")
    @Operation(summary = "Get films by director's id")
    public List<Film> getDirectorsFilms(@PathVariable Integer directorId, @RequestParam String sortBy) {
        return filmService.getDirectorsFilms(directorId, sortBy);
    }
}
