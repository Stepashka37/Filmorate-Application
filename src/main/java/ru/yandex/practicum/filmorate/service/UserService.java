package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.storage.UsersStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersStorage storage;

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public User getUser(Long id) {
        return storage.getUser(id);
    }

    public void deleteUser(Long id) {
        storage.deleteUser(id);
    }

    public void deleteAllUsers() {
        storage.deleteAllUsers();
    }

    public void addFriend(Long id, Long idToAdd) {

        User user = storage.getUser(id);
        User userToAdd = storage.getUser(idToAdd);
        user.addFriend(idToAdd);
        userToAdd.addFriend(id);

    }

    public void deleteFriend(Long id, Long idToDelete) {

        User user = storage.getUser(id);
        User userToDelete = storage.getUser(idToDelete);
        user.deleteFriend(idToDelete);
        userToDelete.deleteFriend(id);
    }

    public List<User> showFriends(Long id) {
        List<User> result = new ArrayList<>();

        User user = storage.getUser(id);
        if (user.getFriends().size() == 0) {
            return new ArrayList<>();
        }
        for (Long idUser : user.getFriends()) {
            result.add(storage.getUser(idUser));

        }
        return result;

    }

    public List<User> showCommonFriends(Long idNumb1, Long idNumb2) {
        List<User> result = new ArrayList<>();
        User userNumb1 = storage.getUser(idNumb1);
        User userNumb2 = storage.getUser(idNumb2);
        if (userNumb1.getFriends() == null || userNumb2.getFriends() == null) {
            return new ArrayList<>();
        }
        Set<Long> intersectSet = new HashSet<>(userNumb1.getFriends());
        intersectSet.retainAll(userNumb2.getFriends());


        for (Long id : intersectSet) {
            result.add(storage.getUser(id));
        }
        return result;
    }
}
