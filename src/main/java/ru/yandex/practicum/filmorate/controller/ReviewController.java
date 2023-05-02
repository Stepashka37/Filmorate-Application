package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.module.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        log.info("Получили запрос на добавление нового ревью");
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("Получили запрос на обновление ревью с id{}", review.getReviewId());
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Integer id) {
        log.info("Получили запрос на удаление ревью с id{}" + id);
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReview(@PathVariable Integer id) {
        log.info("Получили запрос на ревью с id{}", id);
        return reviewService.getReview(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeReview(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получили запрос на лайк на ревью с id{}", id);
        reviewService.likeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получили запрос на удаление лайка ревью с id" + id);
        reviewService.removeLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получили запрос на дислайк на ревью с id{}", id);
        reviewService.dislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получили запрос на удаление дислайка ревью с id" + id);
        reviewService.removeDislike(id, userId);
    }

    @GetMapping
    public List<Review> getReviews(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "0") Integer filmId
    ) {
        log.info("Получили запрос на ревью фильма id{}", filmId);
        return reviewService.getReviews(filmId, count);
    }
}
