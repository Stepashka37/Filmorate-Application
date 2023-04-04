package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.User;

import java.util.List;


public interface UsersStorage {
    List<User> getUsers();

    User addUser(User user) throws ValidationException;

    User updateUser(User user);

    void deleteAllUsers();

    User getUser(Integer id);

    void deleteFriend(int initiator_id, int acceptor_id);

    void deleteUser(Integer id);

    void makeFriendship(int initiator_id, int acceptor_id);
}
