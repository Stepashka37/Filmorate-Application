package ru.yandex.practicum.filmorate.module;

import lombok.Value;

@Value
public class Event {
    int eventId;
    long timestamp;
    int userId;
    int entityId;
    String eventType;
    String operation;
}
