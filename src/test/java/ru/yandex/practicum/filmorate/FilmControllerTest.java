package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.module.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FilmController filmController;


    @AfterEach
    public void clear() {
        filmController.deleteAllFilms();
    }

    @Test
    public void test() {
        assertNotNull(filmController);

    }

    @SneakyThrows
    @Test
    public void filmGetMethodTest() {


        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        mapper.findAndRegisterModules();

        Film film = Film.builder().name("name")
                .likes(new HashSet<>())
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();

        Film filmCreated = Film.builder().name("name")
                .likes(new HashSet<>())
                .id(1L)
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();


        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(get("/films")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();


        String json = result.getResponse().getContentAsString();

        List<Film> films = mapper.readValue(json, new TypeReference<List<Film>>() {
        });

        assertEquals(filmCreated, films.get(0));
    }

    @SneakyThrows
    @Test
    public void filmPostMethodTestValidValue() {
        String validFilm = "{\"likes\":[],\"id\":1,\"name\":\"name\",\"description\":\"Film description\",\"releaseDate\":\"2016-03-04\",\"duration\":120}";

        Film film = Film.builder().name("name")
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        MvcResult result = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andDo(h -> {
                    System.out.println(h.getResponse());
                })
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(validFilm, result.getResponse().getContentAsString());

    }


    @SneakyThrows
    @Test
    public void filmPostMethodTestInvalidName() {
        Film film = Film.builder().name("")
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации name"));


        Film film2 = Film.builder().name(null)
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        gson1 = objectMapper.writeValueAsString(film2);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isInternalServerError());
    }


    @SneakyThrows
    @Test
    public void filmPostMethodTestInvalidDate() {
        Film filmBefore = Film.builder().name("Film")
                .description("Film description")
                .releaseDate(LocalDate.of(1895, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(filmBefore);
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isBadRequest())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации releaseDate"));

        Film filmAfter = Film.builder().name("Film")
                .description("Film description")
                .releaseDate(LocalDate.now().plusDays(1))
                .duration(120)
                .build();
        gson1 = objectMapper.writeValueAsString(filmAfter);
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации releaseDate"));

    }

    @SneakyThrows
    @Test
    public void filmPostMethodTestInvalidDescription() {
        Film film = Film.builder().name("Film")
                .description("The Social Network is a 2010 American biographical drama film directed by David Fincher" +
                        " and written by Aaron Sorkin, based on the 2009 book The Accidental Billionaires by Ben Mezrich." +
                        " It portrays the founding of social networking website Facebook. It stars Jesse Eisenberg as the" +
                        " Facebook founder Mark Zuckerberg, with Andrew Garfield as Eduardo Saverin, Justin Timberlake as" +
                        " Sean Parker, Armie Hammer as Cameron and Tyler Winklevoss, and Max Minghella as Divya Narendra." +
                        " Neither Zuckerberg nor any other Facebook staff were involved with the project, although Saverin" +
                        " was a consultant for Mezrich's book.")
                .releaseDate(LocalDate.of(2010, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации description"));

    }

    @SneakyThrows
    @Test
    public void filmPostMethodTestInvalidDuration() {
        Film film = Film.builder().name("Film")
                .description("Film description")
                .releaseDate(LocalDate.of(2010, 03, 04))
                .duration(-120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации duration"));

    }

    @SneakyThrows
    @Test
    public void filmPutMethodTestInvalidName() {
        Film film = Film.builder().name("Name")
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());


        Film film2 = Film.builder().name(null)
                .id(1L)
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        gson1 = objectMapper.writeValueAsString(film2);
        mockMvc.perform(put("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    public void filmPutMethodTestInvalidDate() {
        Film film = Film.builder().name("Name")
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        Film filmBefore = Film.builder().id(1L)
                .name("name")
                .description("Film description")
                .releaseDate(LocalDate.of(1895, 03, 04))
                .duration(120)
                .build();
        gson1 = objectMapper.writeValueAsString(filmBefore);
        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isBadRequest())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации release date"));

        Film filmAfter = Film.builder().id(1L)
                .name("name")
                .description("Film description")
                .releaseDate(LocalDate.of(2123, 03, 04))
                .duration(120)
                .build();
        gson1 = objectMapper.writeValueAsString(filmAfter);
        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации release date"));
    }

    @SneakyThrows
    @Test
    public void filmPutMethodTestInvalidDescription() {
        Film film = Film.builder().name("Name")
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        Film filmNull = Film.builder().id(1L)
                .name("name")
                .description("\"The Social Network is a 2010 American biographical drama film directed by David Fincher" +
                        "and written by Aaron Sorkin, based on the 2009 book The Accidental Billionaires by Ben Mezrich." +
                        " It portrays the founding of social networking website Facebook. It stars Jesse Eisenberg as the" +
                        " Facebook founder Mark Zuckerberg, with Andrew Garfield as Eduardo Saverin, Justin Timberlake as" +
                        "Sean Parker, Armie Hammer as Cameron and Tyler Winklevoss, and Max Minghella as Divya Narendra." +
                        "Neither Zuckerberg nor any other Facebook staff were involved with the project, although Saverin" +
                        " was a consultant for Mezrich's book.\"")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        gson1 = objectMapper.writeValueAsString(filmNull);
        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации description"));
    }

    @SneakyThrows
    @Test
    public void filmPutMethodTestInvalidDuration() {
        Film film = Film.builder().name("Name")
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        Film filmNull = Film.builder().id(1L)
                .name("name")
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(-120)
                .build();
        gson1 = objectMapper.writeValueAsString(filmNull);
        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isInternalServerError())
                .andExpect(h -> h.getResponse().equals("Ошибка валидации duration"));
    }

    @SneakyThrows
    @Test
    public void filmPutMethodTestInvalidPresence() {
        Film film = Film.builder().name("Name")
                .id(100L)
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isNotFound())
                .andExpect(h -> h.getResponse().equals("Ошибка проверки на наличие"));
    }

    @SneakyThrows
    @Test
    public void filmGetById() {
        Film film = Film.builder().name("Name")
                .likes(new HashSet<>())
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(get("/films/1")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void filmGetByInvalidId() {

        mockMvc.perform(get("/films/100")
                .contentType("application/json")
        ).andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void likeFilm() {
        Film film = Film.builder().name("Name")
                .likes(new HashSet<>())
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(put("/films/1/like/1")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/films/1")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        Film film1 = objectMapper.readValue(json, Film.class);
        assertEquals(1, film1.getLikes().size());
    }

    @SneakyThrows
    @Test
    public void likeFilmInvalidId() {
        Film film = Film.builder().name("Name")
                .likes(new HashSet<>())
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(put("/films/10/like/1")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Test
    public void removeLike() {
        Film film = Film.builder().name("Name")
                .likes(new HashSet<>())
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(put("/films/1/like/1")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/films/1")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        Film film1 = objectMapper.readValue(json, Film.class);
        assertEquals(1, film1.getLikes().size());

        mockMvc.perform(delete("/films/1/like/1")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isOk());

        result = mockMvc.perform(get("/films/1")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isOk())
                .andReturn();

        json = result.getResponse().getContentAsString();

        film1 = objectMapper.readValue(json, Film.class);
        assertEquals(0, film1.getLikes().size());
    }


    @SneakyThrows
    @Test
    public void removeLikeWithInvalidId() {
        Film film = Film.builder().name("Name")
                .likes(new HashSet<>())
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        mockMvc.perform(put("/films/1/like/1")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/films/1")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        Film film1 = objectMapper.readValue(json, Film.class);
        assertEquals(1, film1.getLikes().size());

        mockMvc.perform(delete("/films/100/like/1")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isNotFound());

        mockMvc.perform(delete("/films/1/like/100")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isNotFound());

        result = mockMvc.perform(get("/films/1")
                        .contentType("application/json")
                        .content(gson1)
                ).andExpect(status().isOk())
                .andReturn();

        json = result.getResponse().getContentAsString();

        film1 = objectMapper.readValue(json, Film.class);
        assertEquals(1, film1.getLikes().size());
    }

    @SneakyThrows
    @Test
    public void showPopularFilms() {
        Film film = Film.builder().name("Name")
                .likes(new HashSet<>())
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        String gson1 = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        Film film2 = Film.builder().name("Name2")
                .likes(new HashSet<>())
                .description("Film description")
                .releaseDate(LocalDate.of(2016, 03, 04))
                .duration(120)
                .build();
        gson1 = objectMapper.writeValueAsString(film2);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(get("/films/popular")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        List<Film> films = objectMapper.readValue(json, new TypeReference<List<Film>>() {
        });

        assertEquals(2, films.size());

        mockMvc.perform(put("/films/1/like/1")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isOk());

        mockMvc.perform(put("/films/1/like/2")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isOk());

        mockMvc.perform(put("/films/2/like/3")
                .contentType("application/json")
                .content(gson1)
        ).andExpect(status().isOk());


        result = mockMvc.perform(get("/films/popular?count=1")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();

        json = result.getResponse().getContentAsString();

        films = objectMapper.readValue(json, new TypeReference<List<Film>>() {
        });

        assertEquals(1, films.size());
        assertEquals(1, films.get(0).getId());

        result = mockMvc.perform(get("/films/popular")
                        .contentType("application/json")
                ).andExpect(status().isOk())
                .andReturn();

        json = result.getResponse().getContentAsString();

        films = objectMapper.readValue(json, new TypeReference<List<Film>>() {
        });

        assertEquals(2, films.size());
        assertEquals(1, films.get(0).getId());
        assertEquals(2, films.get(1).getId());

        mockMvc.perform(get("/films/popular?count=-1")
                .contentType("application/json")
        ).andExpect(status().isInternalServerError());

    }
}

