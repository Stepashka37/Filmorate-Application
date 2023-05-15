package ru.yandex.practicum.filmorate.module;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.annotation.ValidReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class Film {
    private double score;
    private int id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
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
                "score=" + score +
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
        return id == film.id && duration == film.duration && Objects.equals(score, film.score) && Objects.equals(name, film.name) && Objects.equals(description, film.description) && releaseDate.equals(film.releaseDate) && Objects.equals(genres, film.genres) && Objects.equals(mpa, film.mpa) && Objects.equals(directors, film.directors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, id, name, description, releaseDate, duration, genres, mpa, directors);
    }
}

