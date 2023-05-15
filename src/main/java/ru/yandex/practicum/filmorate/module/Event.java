package ru.yandex.practicum.filmorate.module;

import lombok.Value;

@Value
public class Event {
    int eventId;
    long timestamp;
    int userId;
    int entityId;
    Operation operation;
    EventType eventType;

    public enum EventType {
        SCORE, FRIEND, REVIEW
    }

    public enum Operation {
        ADD, UPDATE, REMOVE
    }
}
