package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.module.Rating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@Tag(name = "Rating", description = "Request for adult groups raitings")
public class RatingController {
    private final MpaService mpaService;

    @Autowired
    public RatingController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    @Operation(summary = "Get all MPA ratings from database")
    public List<Rating> getRatings() {
        return mpaService.getRatings();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get MPA rating by id")
    public Rating getRatingById(@PathVariable Integer id) {
        return mpaService.getRatingById(id);
    }
}
