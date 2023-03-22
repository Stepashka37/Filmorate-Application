package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService usersService;

    @Autowired
    public UserController(UserService usersService) {
        this.usersService = usersService;
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
        User addedUser = usersService.addUser(user);
        log.info("Добавили пользователя с id{}", addedUser.getId());
        return addedUser;
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
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        usersService.addFriend(id, friendId);
        log.info("Пользователь с id{}", friendId + " добавился в друзья к пользователю с id" + id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long friendId) {
        if (id == null || friendId == null) {
            throw new ValidationException("не указан id");
        }
        usersService.deleteFriend(id, friendId);
        log.info("Пользователь с id{}", friendId + " удалил из друзей пользователя с id" + id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        List<User> friends = usersService.showFriends(id);
        log.info("Получен список друзей пользователя с id{}", id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        List<User> commonFriends = usersService.showCommonFriends(id, otherId);
        log.info("Получен список общих друзей пользователей с id{}", id + " и с id" + otherId);
        return commonFriends;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
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
    public void deleteUser(@PathVariable Long id) {
        if (id <= 0) {
            throw new ValidationException("id должен быть больше или равен 0");
        }
        if (id == null) {
            throw new ValidationException("не указан id");
        }
        usersService.deleteUser(id);
        log.info("Пользователь с id{}" + id + " был удален");

    }


}
