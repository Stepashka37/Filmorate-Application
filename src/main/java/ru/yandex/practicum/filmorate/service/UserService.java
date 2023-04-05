package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UsersStorage;

import java.util.List;

@Service

public class UserService {


    private final UsersStorage storage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UsersStorage storage) {
        this.storage = storage;
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

    public void addFriend(int initiator_id, int acceptor_id) {

        User initiator = storage.getUser(initiator_id);
        User acceptor = storage.getUser(acceptor_id);
        storage.makeFriendship(initiator_id, acceptor_id);

        acceptor.addFriend(initiator_id);

    }

    public void deleteFriend(int initiator_id, int acceptor_id) {

        User user = storage.getUser(initiator_id);
        User userToDelete = storage.getUser(acceptor_id);
        storage.deleteFriend(initiator_id, acceptor_id);
        user.deleteFriend(acceptor_id);
        userToDelete.deleteFriend(initiator_id);
    }

    public List<User> showFriends(Integer id) {
        return storage.showFriends(id);
        /*List<User> result = new ArrayList<>();

        User user = storage.getUser(id);
        if (user.getFriends().size() == 0) {
            return new ArrayList<>();
        }
        for (Integer idUser : user.getFriends()) {
            result.add(storage.getUser(idUser));

        }
        return result;*/

    }

    public List<User> showCommonFriends(Integer idNumb1, Integer idNumb2) {
        List<User> firstUserFriends = storage.showFriends(idNumb1);
        List<User> secondUserFriends = storage.showFriends(idNumb2);
        firstUserFriends.retainAll(secondUserFriends);
        return firstUserFriends;
        /*List<User> result = new ArrayList<>();
        User userNumb1 = storage.getUser(idNumb1);
        User userNumb2 = storage.getUser(idNumb2);
        if (userNumb1.getFriends() == null || userNumb2.getFriends() == null) {
            return new ArrayList<>();
        }
        Set<Integer> intersectSet = new HashSet<>(userNumb1.getFriends());
        intersectSet.retainAll(userNumb2.getFriends());


        for (Integer id : intersectSet) {
            result.add(storage.getUser(id));
        }
        return result;*/
    }
}
