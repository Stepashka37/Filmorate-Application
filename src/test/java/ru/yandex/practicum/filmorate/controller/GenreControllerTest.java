package ru.yandex.practicum.filmorate.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.module.Genre;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GenreController genreController;


    @Test
    public void isPresent() {
        assertNotNull(genreController);
    }

    @SneakyThrows
    @Test
    public void getGenreTest() {
        Genre genreCreated = Genre.builder()
                .id(1)
                .name("Комедия")
                .build();
        MvcResult result = mockMvc.perform(get("/genres/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        Genre genreFromDatabase = objectMapper.readValue(json, Genre.class);

        assertEquals(genreCreated, genreFromDatabase);
    }

    @SneakyThrows
    @Test
    public void getGenresTest() {
        MvcResult result = mockMvc.perform(get("/genres")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        List<Genre> genres = objectMapper.readValue(json, new TypeReference<List<Genre>>() {
        });

        assertEquals(6, genres.size());
    }

}
