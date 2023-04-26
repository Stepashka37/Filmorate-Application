package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.Event;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.storage.interfaces.EventStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UsersStorage;

import java.util.List;

@Service

public class UserService {


    private final UsersStorage storage;
    private final EventStorage eventStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UsersStorage storage, EventStorage eventStorage) {
        this.storage = storage;
        this.eventStorage = eventStorage;
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User addUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getName() == (null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        if (storage.getUser(user.getId()) == null) {
            throw new UserNotFoundException("Пользователь с id" + user.getId() + " не найден");
        }
        return storage.updateUser(user);
    }

    public User getUser(Integer id) {
        return storage.getUser(id);
    }

    public void deleteUser(Integer id) {
        storage.deleteUser(id);
    }

    public void deleteAllUsers() {
        storage.deleteAllUsers();
    }

    public void addFriend(int initiatorId, int acceptorId) {
        User initiator = storage.getUser(initiatorId);
        storage.addFriend(initiatorId, acceptorId);
        eventStorage.addFriend(initiatorId, acceptorId);

    }

    public void deleteFriend(int initiatorId, int acceptorId) {

        User user = storage.getUser(initiatorId);
        User userToDelete = storage.getUser(acceptorId);
        storage.deleteFriend(initiatorId, acceptorId);
        eventStorage.removeFriend(initiatorId, acceptorId);
    }

    public List<User> showFriends(Integer id) {
        return storage.showFriends(id);

    }

    public List<User> showCommonFriends(Integer idNumb1, Integer idNumb2) {
        List<User> firstUserFriends = storage.showFriends(idNumb1);
        List<User> secondUserFriends = storage.showFriends(idNumb2);
        firstUserFriends.retainAll(secondUserFriends);
        return firstUserFriends;
    }

    public List<Event> getEvents(Integer userId) {
        return eventStorage.getEvents(userId);
    }
}
