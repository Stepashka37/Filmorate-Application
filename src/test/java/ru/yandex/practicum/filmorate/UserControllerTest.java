package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.module.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;


    @AfterEach
    public void clear() {
        userController.deleteAllUsers();
    }

    @Test
    public void test() {
        assertNotNull(userController);

    }


    @SneakyThrows
    @Test
    public void userGetMethodTest() {


        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        User user = User.builder().name("name")
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();

        User userCreated = User.builder().name("name")
                .id(1L)
                .email("email@yandex.ru")
                .login("login")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();


        String gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(get("/users")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();


        String json = result.getResponse().getContentAsString();

        List<User> users = mapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(userCreated, users.get(0));
    }

    @SneakyThrows
    @Test
    public void userPostMethodTestValidValue() {
        String validUser = "{\"friends\":[],\"id\":1,\"email\":\"email@yandex.ru\",\"login\":\"login\",\"name\":\"name\",\"birthday\":\"1997-06-05\"}";

        User user = User.builder().name("name")
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andDo(h -> {
                    System.out.println(h.getResponse());
                })
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(validUser, result.getResponse().getContentAsString());
    }

    @SneakyThrows
    @Test
    public void userPostMethodTestInvalidEmail() {
        User user = User.builder().name("name")
                .email("")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации email"));

        user = User.builder().name("name")
                .email("это-неправильный?эмейл@")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации email"));

        User userNull = User.builder().name("name")
                .email("email.yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(userNull);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации email"));
    }

    @SneakyThrows
    @Test
    public void userPostMethodTestInvalidLogin() {
        User user = User.builder().name("name")
                .email("email@yandex.ru")
                .login("")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации login"));

        User userNull = User.builder().name("name")
                .email("email.yandex.ru")
                .login("login with space")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(userNull);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации login"));
    }

    @SneakyThrows
    @Test
    public void userPostMethodTestInvalidBirthday() {
        User user = User.builder().name("name")
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        String gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации birthday"));


    }

    @SneakyThrows
    @Test
    public void userPostMethodTestNameAsLogin() {

        User user1 = User.builder().name("")
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();

        String gson1 = objectMapper.writeValueAsString(user1);
        MvcResult result1 = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isCreated())
                .andReturn();

        User user2 = User.builder().name(null)
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();

        String gson2 = objectMapper.writeValueAsString(user2);
        MvcResult result2 = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(gson2)
                ).andExpect(status().isCreated())
                .andReturn();

        User userCreated = User.builder().name("login")
                .id(1L)
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();

        User fromGson1 = objectMapper.readValue(result1.getResponse().getContentAsString(), User.class);
        User fromGson2 = objectMapper.readValue(result2.getResponse().getContentAsString(), User.class);
        assertEquals(userCreated, fromGson1);
        userCreated.setId(2L);
        assertEquals(userCreated, fromGson2);


    }

    @SneakyThrows
    @Test
    public void userPutMethodTestInvalidEmail() {
        User user = User.builder().name("name")
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User userToPut = User.builder().name("name")
                .id(1L)
                .email("email.yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(userToPut);
        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации email"));

        userToPut = User.builder().name("name")
                .id(1L)
                .email("")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(userToPut);
        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации email"));

        userToPut = User.builder().name("name")
                .id(1L)
                .email("это-неправильный?эмейл@")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(userToPut);
        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации email"));
    }

    @SneakyThrows
    @Test
    public void userPutMethodTestInvalidLogin() {
        User user = User.builder().name("name")
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User userInvalidLogin = User.builder().name("name")
                .id(1L)
                .email("email.yandex.ru")
                .login("")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(userInvalidLogin);
        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации login"));

        userInvalidLogin = User.builder().name("name")
                .id(1L)
                .email("email.yandex.ru")
                .login("login with space")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(userInvalidLogin);
        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации login"));
    }

    @SneakyThrows
    @Test
    public void userPutMethodTestInvalid() {
        User user = User.builder().name("name")
                .email("email@yandex.ru")
                .id(9999L)
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(put("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void userPutMethodTestInvalidBirthday() {
        User user = User.builder().name("name")
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User userInvalidBirthday = User.builder().name("name")
                .id(1L)
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        gson1 = objectMapper.writeValueAsString(userInvalidBirthday);
        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации birthday"));
    }

    @SneakyThrows
    @Test
    public void userPutMethodTestInvalidPresence() {
        User user = User.builder().name("name")
                .id(10L)
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isNotFound())
                .andExpect(h -> h.getResponse().equals("Ошибка проверки на наличие"));
    }

    @SneakyThrows
    @Test
    public void getUserByIdTest() {
        User user = User.builder().name("name")
                .email("email@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(get("/users/1")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void getUserByInvalidIdTest() {


        mockMvc.perform(get("/users/100")
                .contentType("application/json")
        ).andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void addFriendAndShowFriends() {
        User user1 = User.builder().name("name1")
                .email("email1@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User user2 = User.builder().name("name2")
                .email("email2@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(put("/users/1/friends/2")
                .contentType("application/json")
        ).andExpect(status().isOk());


        MvcResult result = mockMvc.perform(get("/users/1/friends")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();

        List<User> users = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(1, users.size());
        assertEquals(2, users.get(0).getId());

        result = mockMvc.perform(get("/users/2/friends")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        json = result.getResponse().getContentAsString();

        users = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());


    }

    @SneakyThrows
    @Test
    public void addFriendAndShowFriendWithInvalidId() {
        User user1 = User.builder().name("name1")
                .email("email1@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());


        mockMvc.perform(put("/users/1/friends/100")
                .contentType("application/json")
        ).andExpect(status().isNotFound());


        MvcResult result = mockMvc.perform(get("/users/1/friends")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();

        List<User> users = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(0, users.size());


    }


    @SneakyThrows
    @Test
    public void addFriendAndShowCommonFriends() {
        User user1 = User.builder().name("name1")
                .email("email1@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User user2 = User.builder().name("name2")
                .email("email2@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User user3 = User.builder().name("name3")
                .email("email3@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(put("/users/1/friends/2")
                .contentType("application/json")
        ).andExpect(status().isOk());

        mockMvc.perform(put("/users/1/friends/3")
                .contentType("application/json")
        ).andExpect(status().isOk());

        mockMvc.perform(put("/users/2/friends/3")
                .contentType("application/json")
        ).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/users/1/friends/common/2")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();

        List<User> commonfFriends = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(1, commonfFriends.size());
        assertEquals(3, commonfFriends.get(0).getId());

        result = mockMvc.perform(get("/users/2/friends/common/1")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        json = result.getResponse().getContentAsString();

        commonfFriends = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(1, commonfFriends.size());
        assertEquals(3, commonfFriends.get(0).getId());
    }


    @SneakyThrows
    @Test
    public void addFriendAndShowCommonFriendsWithInvalidId() {
        User user1 = User.builder().name("name1")
                .email("email1@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User user2 = User.builder().name("name2")
                .email("email2@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User user3 = User.builder().name("name3")
                .email("email3@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(put("/users/1/friends/2")
                .contentType("application/json")
        ).andExpect(status().isOk());

        mockMvc.perform(put("/users/1/friends/100")
                .contentType("application/json")
        ).andExpect(status().isNotFound());

        mockMvc.perform(put("/users/2/friends/100")
                .contentType("application/json")
        ).andExpect(status().isNotFound());

        MvcResult result = mockMvc.perform(get("/users/1/friends/common/2")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();

        List<User> commonfFriends = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });


        assertEquals(0, commonfFriends.size());

        result = mockMvc.perform(get("/users/2/friends/common/1")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        json = result.getResponse().getContentAsString();

        commonfFriends = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(0, commonfFriends.size());

        mockMvc.perform(get("/users/1/friends/common/100")
                .contentType("application/json")
        ).andExpect(status().isNotFound());

        mockMvc.perform(get("/users/100/friends/common/1")
                .contentType("application/json")
        ).andExpect(status().isNotFound());


    }

    @SneakyThrows
    @Test
    public void ShowCommonFriendsEmpty() {
        User user1 = User.builder().name("name1")
                .email("email1@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User user2 = User.builder().name("name2")
                .email("email2@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User user3 = User.builder().name("name3")
                .email("email3@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(put("/users/1/friends/2")
                .contentType("application/json")
        ).andExpect(status().isOk());

        mockMvc.perform(put("/users/1/friends/3")
                .contentType("application/json")
        ).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/users/1/friends/common/2")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();

        List<User> commonfFriends = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(0, commonfFriends.size());

        result = mockMvc.perform(get("/users/2/friends/common/3")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        json = result.getResponse().getContentAsString();

        commonfFriends = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(1, commonfFriends.size());
        assertEquals(1, commonfFriends.get(0).getId());

    }

    @SneakyThrows
    @Test
    public void deleteFriend() {
        User user1 = User.builder().name("name1")
                .email("email1@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        User user2 = User.builder().name("name2")
                .email("email2@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        gson1 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(put("/users/1/friends/2")
                .contentType("application/json")
        ).andExpect(status().isOk());


        MvcResult result = mockMvc.perform(get("/users/1/friends")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();

        List<User> users = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(1, users.size());
        assertEquals(2, users.get(0).getId());

        mockMvc.perform(delete("/users/1/friends/2")
                .contentType("application/json")
        ).andExpect(status().isOk());

        result = mockMvc.perform(get("/users/1/friends")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        json = result.getResponse().getContentAsString();

        users = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(0, users.size());

        result = mockMvc.perform(get("/users/2/friends")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();
        json = result.getResponse().getContentAsString();

        users = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });

        assertEquals(0, users.size());


    }

    @SneakyThrows
    @Test
    public void deleteFriendWithInvalidId() {
        User user1 = User.builder().name("name1")
                .email("email1@yandex.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1997, 06, 05))
                .build();
        String gson1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(delete("/users/1/friends/100")
                .contentType("application/json")
        ).andExpect(status().isNotFound());

        mockMvc.perform(delete("/users/100/friends/1")
                .contentType("application/json")
        ).andExpect(status().isNotFound());
    }
}
