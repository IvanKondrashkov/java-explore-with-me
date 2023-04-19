package ru.practicum.event.service;

import java.util.List;
import ru.practicum.event.dto.EventFullDto;

public interface EventServicePublicApi {
    /**
     * Find event by id.
     * @param id Event id.
     * @return EventFullDto.
     */
    EventFullDto findById(Long id);

    /**
     * Find all events.
     * @param text Search in the annotation and a detailed description of the event.
     * @param categories List Category ids.
     * @param paid Only paid/free events.
     * @param rangeStart Date and time not earlier than when the event should occur.
     * @param rangeEnd Date and time no later than which the event should occur.
     * @param onlyAvailable Only events that have not reached the limit of participation requests. Default value : false.
     * @param sort Sorting option: by event date or by number of views.
     * @param from Number of events that need to be skipped to form the current set. Default value : 0.
     * @param size Number of events in the set. Default value : 10.
     * @return List EventFullDto.
     */
    List<EventFullDto> findAll(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                                  Boolean onlyAvailable, String sort, Integer from, Integer size);
}