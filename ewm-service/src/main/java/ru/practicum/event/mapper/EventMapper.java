package ru.practicum.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Location;
import ru.practicum.user.model.User;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.event.model.State;
import ru.practicum.event.model.Event;
import ru.practicum.category.model.Category;
import ru.practicum.category.mapper.CategoryMapper;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class EventMapper {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventShortDto toEventShortDto(Event event, Integer views, Integer confirmedRequests) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                ofStringByPattern(event.getEventDate()),
                event.getPaid(),
                views,
                confirmedRequests,
                CategoryMapper.toCategoryDto(event.getCategory()),
                UserMapper.toUserShortDto(event.getInitiator())
        );
    }

    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category, Location location) {
        return new Event(
                null,
                newEventDto.getTitle(),
                newEventDto.getAnnotation(),
                newEventDto.getDescription(),
                ofDateTimeByPattern(newEventDto.getEventDate()),
                LocalDateTime.now(),
                null,
                newEventDto.getPaid(),
                newEventDto.getRequestModeration(),
                newEventDto.getParticipantLimit(),
                0,
                State.PENDING,
                location,
                category,
                initiator
        );
    }

    public static EventFullDto toEventFullDto(Event event, Integer views, Integer confirmedRequests) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getDescription(),
                ofStringByPattern(event.getEventDate()),
                ofStringByPattern(event.getCreatedOn()),
                event.getPublishedOn() == null ? null : ofStringByPattern(event.getPublishedOn()),
                event.getPaid(),
                event.getRequestModeration(),
                event.getParticipantLimit(),
                event.getState().name(),
                views,
                confirmedRequests,
                LocationMapper.toLocationDto(event.getLocation()),
                CategoryMapper.toCategoryDto(event.getCategory()),
                UserMapper.toUserShortDto(event.getInitiator())
        );
    }

    private static LocalDateTime ofDateTimeByPattern(String date) {
        return LocalDateTime.parse(date, FORMATTER);
    }

    private static String ofStringByPattern(LocalDateTime date) {
        return date.format(FORMATTER);
    }
}