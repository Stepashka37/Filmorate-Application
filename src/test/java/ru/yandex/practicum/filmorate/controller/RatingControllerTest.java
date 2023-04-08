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
import ru.yandex.practicum.filmorate.module.Rating;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class RatingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RatingController ratingController;

    @SneakyThrows
    @Test
    public void getRatingTest() {
        Rating ratingCreated = Rating.builder()
                .id(1)
                .name("G")
                .build();
        MvcResult result = mockMvc.perform(get("/mpa/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        Rating ratingFromDatabase = objectMapper.readValue(json, Rating.class);

        assertEquals(ratingCreated, ratingFromDatabase);
    }

    @SneakyThrows
    @Test
    public void getRatingsTest() {
        MvcResult result = mockMvc.perform(get("/mpa")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        List<Rating> ratings = objectMapper.readValue(json, new TypeReference<List<Rating>>() {
        });

        assertEquals(5, ratings.size());
    }
}
