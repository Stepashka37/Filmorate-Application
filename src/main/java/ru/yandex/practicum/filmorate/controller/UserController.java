package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.Event;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Requests for users")
public class UserController {

    private final UserService usersService;
    private final FilmService filmService;

    @Autowired
    public UserController(UserService usersService, FilmService filmService) {
        this.usersService = usersService;
        this.filmService = filmService;
    }

    @GetMapping
    @Operation(summary = "Get list of all users")
    public List<User> getUsers() {
        List<User> users = usersService.getUsers();
        log.info("Получили список всех пользователей");
        return users;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add new user to database")
    public User addUser(@Valid @RequestBody User user) {
        User addUser = usersService.addUser(user);
        log.info("Добавили пользователя с id{}", addUser.getId());
        return addUser;
    }

    @PutMapping()
    @ResponseBody
    @Operation(summary = "Update user in database")
    public User updateUser(@Valid @RequestBody User user) {
        User updatedUser = usersService.updateUser(user);
        log.info("Обновили данные пользователя с id{}", updatedUser.getId());
        return updatedUser;
    }

    @DeleteMapping("")
    @Operation(summary = "Delete all users")
    public void deleteAllUsers() {
        usersService.deleteAllUsers();
        log.info("Все пользователи из базы удалены");

    }

    @PutMapping("/{id}/friends/{friendId}")
    @Operation(summary = "Friendship between 2 users")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        usersService.addFriend(id, friendId);
        log.info("Пользователь с id{} добавился в друзья к пользователю с id{}" + friendId, id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @Operation(summary = "Delete from friends' list")
    public void deleteFriend(@PathVariable @Min(1) Integer id, @PathVariable @Min(1) Integer friendId) {
        if (id == null || friendId == null) {
            throw new ValidationException("не указан id");
        }
        usersService.deleteFriend(id, friendId);
        log.info("Пользователь с id{} удалил из друзей пользователя с id{}" + friendId, id);
    }

    @GetMapping("/{id}/friends")
    @Operation(summary = "Get list of user friends by id")
    public List<User> getFriends(@PathVariable Integer id) {
        List<User> friends = usersService.showFriends(id);
        log.info("Получен список друзей пользователя с id{}", id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @Operation(summary = "Get common friends between 2 users")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        List<User> commonFriends = usersService.showCommonFriends(id, otherId);
        log.info("Получен список общих друзей пользователей с id{} и с id{}", id, otherId);
        return commonFriends;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
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
    @Operation(summary = "Delete user by id")
    public void deleteUser(@PathVariable Integer id) {
        if (id <= 0) {
            throw new ValidationException("id должен быть больше или равен 0");
        }
        if (id == null) {
            throw new ValidationException("не указан id");
        }
        usersService.deleteUser(id);
        log.info("Пользователь с id{} был удален", id);

    }

    @GetMapping("/{id}/recommendations")
    @Operation(summary = "Recommend films to user by id")
    public List<Film> recommendFilms(@PathVariable(value = "id") Integer userId) {
        return filmService.recommendFilms(userId);
    }

    @GetMapping("/{id}/feed")
    @Operation(summary = "Get list of user actions")
    public List<Event> getEvents(@PathVariable Integer id) {
        return usersService.getEvents(id);
    }
}
