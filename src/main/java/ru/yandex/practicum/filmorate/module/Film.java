package ru.yandex.practicum.filmorate.module;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import ru.yandex.practicum.filmorate.annotation.ValidReleaseDate;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class Film {
    @Singular
    private Set<Integer> likes = new HashSet<>();
    //@Positive(message = "id должен быть больше нуля")
    @Id
    private int id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Size(min = 0, max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;
    @Past(message = "Дата выхода фильма не может быть в будущем")
    @NonNull
    @ValidReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    @NonNull
    private long duration;

    private Set<Genre> genres;


    private RatingMpa mpa;


    @Override
    public String toString() {
        return "Film{" +
                "likes=" + likes +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration && Objects.equals(likes, film.likes) && Objects.equals(id, film.id) && Objects.equals(name, film.name) && Objects.equals(description, film.description) && releaseDate.equals(film.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(likes, id, name, description, releaseDate, duration);
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

