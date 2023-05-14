package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({UserNotFoundException.class, FilmNotFoundException.class, GenreNotFoundException.class, MpaNotFoundException.class, ReviewNotFoundException.class, DirectorNotFoundException.class})


    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFoundExc(final RuntimeException p) {
        log.error("404: " + p.getMessage());
        return new ErrorResponse("Объект не найден", p.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationExc(final ValidationException p) {
        log.error("400: " + p.getMessage());
        return new ErrorResponse("Ошибка валидации", p.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse anyOtherExc(final Exception p) {
        log.error("500: " + p.getMessage());
        return new ErrorResponse("Произошла ошибка!", p.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse annotationValidationExc(MethodArgumentNotValidException exc) {
        log.error("400: " + exc.getMessage());
        return new ErrorResponse("Ошибка валидации с помощью аннотаций", exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse annotationValidationExc(ConstraintViolationException exc) {
        log.error("400: " + exc.getMessage());
        return new ErrorResponse("Ошибка валидации с помощью аннотаций", exc.getMessage());
    }
}
