package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UsersStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Component
public class UserDbStorage implements UsersStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(Integer id) {
        String sql = "select * from USERS where USER_ID = ?";
        return jdbcTemplate.query("select * from USERS where USER_ID = ?", (rs, rowNum) -> makeUser(rs), id)
                .stream().findAny().orElseThrow(() -> new UserNotFoundException("Пользователь с id" + id + "не найден"));
    }

    @Override
    public List<User> getUsers() {
        String sql = "select * from USERS ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User addUser(User user) {
        String sql = "insert into users (LOGIN, NAME, EMAIL, BIRTHDAY)" +
                "values (?, ?, ?, ?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        int key = keyHolder.getKey().intValue();


        return jdbcTemplate.query("select * from USERS where USER_ID = ?", (rs, rowNum) -> makeUser(rs), key)
                .stream().findAny().orElse(null);
    }

    @Override
    public User updateUser(User user) {
        String sql = "update USERS  set " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return jdbcTemplate.query("select * from users where user_id = ?", (rs, rowNum) -> makeUser(rs), user.getId())
                .stream().findAny().orElseThrow(() -> new UserNotFoundException("Пользователь с id" + user.getId() + " не найден"));
    }

    @Override
    public void deleteAllUsers() {

        String sql = "SET REFERENTIAL_INTEGRITY FALSE;" +
                "TRUNCATE TABLE   USERS; " +
                "TRUNCATE TABLE FILM_LIKES; " +
                "TRUNCATE TABLE USER_FRIENDS; " +
                "SET REFERENTIAL_INTEGRITY FALSE;" +
                "alter table USERS alter column USER_ID restart with 1";

        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteUser(Integer id) {
        String sqlDeleteUser = "delete from USERS where USER_ID = ?";
        String sqlDeleteUserFromFriends1 = "delete from USER_FRIENDS where INITIATOR_ID = ?";
        String sqlDeleteUserFromFriends2 = "delete from USER_FRIENDS where ACCEPTOR_ID = ?";
        String sqlDeleteUserLike = "delete from FILM_LIKES where USER_ID = ?";
        jdbcTemplate.update(sqlDeleteUserFromFriends1, id);
        jdbcTemplate.update(sqlDeleteUserFromFriends2, id);
        jdbcTemplate.update(sqlDeleteUser, id);
        jdbcTemplate.update(sqlDeleteUserLike, id);
    }

    @Override
    public void addFriend(int initiatorId, int acceptorId) {
        String checkUser = "select * from USERS where USER_ID = ?";

        jdbcTemplate.query("select * from USERS where USER_ID = ?", (rs, rowNum) -> makeUser(rs), initiatorId)
                .stream().findAny().orElseThrow(() -> new UserNotFoundException("Пользователь с id" + initiatorId + "не найден"));
        jdbcTemplate.query("select * from USERS where USER_ID = ?", (rs, rowNum) -> makeUser(rs), acceptorId)
                .stream().findAny().orElseThrow(() -> new UserNotFoundException("Пользователь с id" + acceptorId + "не найден"));

        String sqlLeaveRequest = " merge into USER_FRIENDS (INITIATOR_ID, ACCEPTOR_ID, STATUS)" +
                "values (?, ?, false)";
        jdbcTemplate.update(sqlLeaveRequest, initiatorId, acceptorId);

    }

    @Override
    public void deleteFriend(int initiatorId, int acceptorId) {
        String sqlDeleteFriend = "delete from USER_FRIENDS " +
                "where INITIATOR_ID = ? AND ACCEPTOR_ID = ?";
        jdbcTemplate.update(sqlDeleteFriend, initiatorId, acceptorId);
    }

    @Override
    public List<User> showFriends(int id) {
        jdbcTemplate.query("select * from USERS where USER_ID = ?", (rs, rowNum) -> makeUser(rs), id)
                .stream().findAny().orElseThrow(() -> new UserNotFoundException("Пользователь с id" + id + "не найден"));

        String sqlShowFriends = "select USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY  from USERS " +
                "join USER_FRIENDS UF on USERS.USER_ID = UF.ACCEPTOR_ID" +
                " where INITIATOR_ID = ?";
        return jdbcTemplate.query(sqlShowFriends, (rs, rowNum) -> makeUser(rs), id);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User userBuilt = User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
        String sqlQueryForFriends = "select ACCEPTOR_ID from USER_FRIENDS where INITIATOR_ID = " + userBuilt.getId();
        userBuilt.setFriends(new HashSet<>(jdbcTemplate.queryForList(sqlQueryForFriends, Integer.class)));

        return userBuilt;
    }
}
