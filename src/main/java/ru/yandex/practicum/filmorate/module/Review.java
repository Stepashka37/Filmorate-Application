package ru.yandex.practicum.filmorate.module;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@RequiredArgsConstructor
public class Review {

    private Integer reviewId;

    @NonNull
    @NotBlank
    @Size(min = 1, max = 5000)
    private String content;

    @NonNull
    @JsonProperty(value = "isPositive")
    private Boolean isPositive;

    @NotNull
    private Integer userId;

    @NonNull
    private Integer filmId;

    private int useful;

    public Review() {
        useful = 0;
    }

    public Review(Integer reviewId, String content, boolean isPositive, Integer userId, Integer filmId, Integer useful) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
        this.useful = useful;
    }
}
