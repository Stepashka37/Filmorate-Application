package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.module.Event;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService usersService;
    private final FilmService filmService;

    @Autowired
    public UserController(UserService usersService, FilmService filmService) {
        this.usersService = usersService;
        this.filmService = filmService;
    }

    @GetMapping
    public List<User> getUsers() {
        List<User> users = usersService.getUsers();
        log.info("Получили список всех пользователей");
        return users;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody User user) {
        User addUser = usersService.addUser(user);
        log.info("Добавили пользователя с id{}", addUser.getId());
        return addUser;
    }

    @PutMapping()
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) {
        User updatedUser = usersService.updateUser(user);
        log.info("Обновили данные пользователя с id{}", updatedUser.getId());
        return updatedUser;
    }

    @DeleteMapping("")
    public void deleteAllUsers() {
        usersService.deleteAllUsers();
        log.info("Все пользователи из базы удалены");

    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        usersService.addFriend(id, friendId);
        log.info("Пользователь с id{}", friendId + " добавился в друзья к пользователю с id" + id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable @Min(1) Integer id, @PathVariable @Min(1) Integer friendId) {
        if (id == null || friendId == null) {
            throw new ValidationException("не указан id");
        }
        usersService.deleteFriend(id, friendId);
        log.info("Пользователь с id{}", friendId + " удалил из друзей пользователя с id" + id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        List<User> friends = usersService.showFriends(id);
        log.info("Получен список друзей пользователя с id{}", id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        List<User> commonFriends = usersService.showCommonFriends(id, otherId);
        log.info("Получен список общих друзей пользователей с id{}", id + " и с id" + otherId);
        return commonFriends;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        if (id <= 0) {
            throw new ValidationException("id должен быть больше или равен 0");
        }
        if (id == null) {
            throw new ValidationException("не указан id");
        }
        User user = usersService.getUser(id);
        log.info("Получен пользователь с id{}", id);
        return user;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        if (id <= 0) {
            throw new ValidationException("id должен быть больше или равен 0");
        }
        if (id == null) {
            throw new ValidationException("не указан id");
        }
        usersService.deleteUser(id);
        log.info("Пользователь с id{}" + id + " был удален");

    }

    @GetMapping("/{id}/recommendations")
    public List<Film> recommendFilms(@PathVariable(value = "id") Integer userId) {
        return filmService.recommendFilms(userId);
    }

    @GetMapping("/{id}/feed")
    public List<Event> getEvents(@PathVariable Integer id) {
        log.info("Получена лента событий для пользователя с id{}", id);
        return usersService.getEvents(id);
    }
}
