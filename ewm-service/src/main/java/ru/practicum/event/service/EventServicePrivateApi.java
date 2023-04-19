package ru.practicum.event.service;

import java.util.List;
import ru.practicum.event.dto.*;

public interface EventServicePrivateApi {
    /**
     * Find event by the current user
     * @param userId User id.
     * @param id Event id.
     * @return EventFullDto.
     */
    EventFullDto findById(Long userId, Long id);

    /**
     * Find all events by the current user.
     * @param userId User id.
     * @param from Number of events that need to be skipped to form the current set. Default value : 0.
     * @param size Number of events in the set. Default value : 10.
     * @return List EventShortDto.
     */
    List<EventShortDto> findAll(Long userId, Integer from, Integer size);

    /**
     * Create event.
     * @param newEventDto Entity dto.
     * @param userId User id.
     * @return EventFullDto.
     */
    EventFullDto save(NewEventDto newEventDto, Long userId);

    /**
     * Update event by the current user
     * @param eventUserRequest Entity dto.
     * @param userId User id.
     * @param id Event id.
     * @return EventFullDto.
     */
    EventFullDto update(UpdateEventUserRequest eventUserRequest, Long userId, Long id);
}