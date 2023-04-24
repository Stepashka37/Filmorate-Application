package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;


    @BeforeEach
    public void setData() {
        User user1 = User.builder()
                .email("yandex1@yandex.ru")
                .login("user1")
                .name("user1")
                .birthday(LocalDate.of(1997, 06, 03))
                .build();

        User user2 = User.builder()
                .email("yandex2@yandex.ru")
                .login("user2")
                .name("user2")
                .birthday(LocalDate.of(1996, 06, 03))
                .build();

        User user3 = User.builder()
                .email("yandex3@yandex.ru")
                .login("user3")
                .name("user3")
                .birthday(LocalDate.of(1995, 06, 03))
                .build();

        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);
        userDbStorage.addUser(user3);

    }

    @AfterEach
    public void clearAndResetId() {
        userDbStorage.deleteAllUsers();
    }

    @Test
    public void getUserTest() {
        User user = userDbStorage.getUser(1);

        assertNotNull(user);
        assertEquals(1, user.getId());
    }

    @Test
    public void getUsersTest() {
        List<User> users = userDbStorage.getUsers();

        assertEquals(3, users.size());
    }

    @Test
    public void addUserTest() {
        User user1 = User.builder()
                .email("user1@yandex.ru")
                .login("user1")
                .name("user1")
                .birthday(LocalDate.of(1998, 06, 03))
                .build();
        User user1Created = userDbStorage.addUser(user1);

        assertEquals(4, user1Created.getId());
        assertEquals(4, userDbStorage.getUsers().size());

    }

    @Test
    public void updateUserTest() {
        User userUpd = User.builder()
                .id(1)
                .email("user1Upd@yandex.ru")
                .login("user1Upd")
                .name("user1Upd")
                .birthday(LocalDate.of(1998, 06, 03))
                .build();
        User userUpdated = userDbStorage.updateUser(userUpd);

        assertEquals("user1Upd@yandex.ru", userUpdated.getEmail());
        assertEquals(3, userDbStorage.getUsers().size());
    }

    @Test
    public void deleteUserTest() {
        userDbStorage.deleteUser(1);

        assertEquals(2, userDbStorage.getUsers().size());
    }

    @Test
    public void deleteUsersTest() {
        userDbStorage.deleteAllUsers();

        assertEquals(0, userDbStorage.getUsers().size());
    }

    @Test
    public void addFriendTest() {
        userDbStorage.addFriend(1, 2);
        List<User> user1Friends = userDbStorage.showFriends(1);
        List<User> user2Friends = userDbStorage.showFriends(2);

        assertEquals(1, user1Friends.size());
        assertEquals(0, user2Friends.size());
    }

    @Test

    public void deleteFriendTest() {
        userDbStorage.addFriend(1, 2);
        userDbStorage.addFriend(2, 1);
        List<User> user1Friends = userDbStorage.showFriends(1);
        List<User> user2Friends = userDbStorage.showFriends(2);

        assertEquals(1, user1Friends.size());
        assertEquals(1, user2Friends.size());

        userDbStorage.deleteFriend(1, 2);
        user1Friends = userDbStorage.showFriends(1);
        user2Friends = userDbStorage.showFriends(2);

        assertEquals(0, user1Friends.size());
        assertEquals(1, user2Friends.size());
    }

    @Test
    public void showFriendsTest() {
        userDbStorage.addFriend(1, 2);
        List<User> user1Friends = userDbStorage.showFriends(1);
        List<User> user2Friends = userDbStorage.showFriends(2);

        assertEquals(1, user1Friends.size());
        assertEquals(0, user2Friends.size());
    }
}

