package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UsersStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UsersStorage {


    private final Map<Integer, User> users = new HashMap<>();
    private Integer genId = 0;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }


    @Override
    public User addUser(User user) {

        validateUser(user);
        ++genId;
        user.setId(genId);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User getUser(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        return users.get(id);
    }

    @Override
    public void deleteFriend(int initiatorId, int acceptorId) {
        users.get(initiatorId).deleteFriend(acceptorId);
        users.get(acceptorId).deleteFriend(initiatorId);
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        if (!users.containsKey(user.getId())) {

            throw new UserNotFoundException("Пользователь с id" + user.getId() + " не найден");
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getName() == (null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
        genId = 0;
    }


    @Override
    public void deleteUser(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с id" + id + " не найден");
        }
        users.remove(id);
    }

    @Override
    public void addFriend(int initiatorId, int acceptorId) {
        if (!users.containsKey(initiatorId)) {
            throw new UserNotFoundException("Пользователь с id" + initiatorId + " не найден");
        }
        if (!users.containsKey(acceptorId)) {
            throw new UserNotFoundException("Пользователь с id" + acceptorId + " не найден");
        }
        users.get(acceptorId).addFriend(initiatorId);
    }

    @Override
    public List<User> showFriends(int id) {
        List<User> friends = new ArrayList<>();
        for (Integer integer : users.get(id).getFriends()) {
            friends.add(users.get(integer));
        }
        return friends;
    }

}
