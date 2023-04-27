package ru.yandex.practicum.filmorate.module;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor

public class User {
    @Singular
    private Set<Integer> friends;
    @Positive(message = "id должен быть больше нуля")

    private Integer id;
    @Email(message = "Email не может быть пустым и должен соответствовать следующему формату: email@email.ru")

    @NotBlank
    private String email;

    @NotBlank
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private boolean status;


    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(Set<Integer> friends, Integer id, String email, String login, String name, LocalDate birthday, boolean status) {
        this.friends = friends;
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "friends=" + friends +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(friends, user.friends) && Objects.equals(email, user.email) && Objects.equals(login, user.login) && Objects.equals(name, user.name) && Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friends, id, email, login, name, birthday);
    }

    public Set<Integer> getFriends() {
        return friends;
    }

    public void addFriend(Integer id) {
        if (friends.isEmpty()) {
            friends = new HashSet<>();
            friends.add(id);
            return;
        }
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        if (!friends.contains(id)) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        friends.remove(id);
    }


}
