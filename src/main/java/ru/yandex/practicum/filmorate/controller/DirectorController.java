package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.module.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
@Tag(name = "Directors", description = "Requests for directors")
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    @Operation(summary = "Get list of all directors")
    public List<Director> getDirectors() {
        List<Director> directors = directorService.getDirectors();
        log.info("Получили список всех режиссеров");
        return directors;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get director by id")
    public Director getDirectorById(@PathVariable int id) {
        Director director = directorService.getDirectorById(id);
        log.info("Получили режиссера с id{}", id);
        return director;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add new director to database")
    public Director addDirector(@Valid @RequestBody Director director) {
        Director newDirector = directorService.addDirector(director);
        log.info("Добавили режиссера с id{}", newDirector.getId());
        return newDirector;
    }

    @PutMapping
    @Operation(summary = "Update director in database")
    public Director updateDirector(@Valid @RequestBody Director director) {
        Director updatedDirector = directorService.updateDirector(director);

        log.info("Обновили режиссера с id{}", director.getId());
        return updatedDirector;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete director by id")
    public void deleteDirector(@PathVariable int id) {
        directorService.deleteDirector(id);
        log.info("Удалили режиссера с id{}", id);
    }

}
