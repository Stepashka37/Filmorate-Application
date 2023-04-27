package ru.yandex.practicum.filmorate.module;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@RequiredArgsConstructor
public class Review {

    private int reviewId;

    @NonNull
    @Size(min = 1, max = 5000)
    private String content;

    @NotNull
    @JsonProperty(value="isPositive")
    private boolean isPositive;

    @NotNull
    private int userId;

    @NotNull
    private int filmId;

    private int useful;

    public Review() {
        useful = 0;
    }

    public Review(int reviewId, String content, boolean isPositive, int userId, int filmId, int useful) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
        this.useful = useful;
    }
}
