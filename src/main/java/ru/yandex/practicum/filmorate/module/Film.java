package ru.yandex.practicum.filmorate.module;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import ru.yandex.practicum.filmorate.annotation.ValidReleaseDate;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class Film {
    @Singular
    private Set<Integer> likes = new HashSet<>();
    private int id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Size(min = 0, max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;
    @NonNull
    @ValidReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    @NonNull
    private long duration;

    private Set<Genre> genres;

    @NotNull
    private Rating mpa;

    private Set<Director> directors;

    @Override
    public String toString() {
        return "Film{" +
                "likes=" + likes +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", genres=" + genres +
                ", mpa=" + mpa +
                ", directors=" + directors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id && duration == film.duration && Objects.equals(likes, film.likes) && Objects.equals(name, film.name) && Objects.equals(description, film.description) && releaseDate.equals(film.releaseDate) && Objects.equals(genres, film.genres) && Objects.equals(mpa, film.mpa) && Objects.equals(directors, film.directors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(likes, id, name, description, releaseDate, duration, genres, mpa, directors);
    }

    public void likeFilm(int userId) {
        if (likes.isEmpty()) {
            likes = new HashSet<>();
            likes.add(userId);
            return;
        }
        likes.add(userId);
    }

    public void removeLike(int userId) {
        if (!likes.contains(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        likes.remove(userId);
    }


}

