package ru.practicum.event.service;

import java.util.List;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

public interface EventServiceAdminApi {
    /**
     * Find all events.
     * @param users List User ids.
     * @param states List Event state.
     * @param categories List Category ids.
     * @param rangeStart Date and time not earlier than when the event should occur.
     * @param rangeEnd Date and time no later than which the event should occur.
     * @param from Number of events that need to be skipped to form the current set. Default value : 0.
     * @param size Number of events in the set. Default value : 10.
     * @return List EventFullDto.
     */
    List<EventFullDto> findAll(List<Long> users, List<String> states, List<Long> categories,
                                  String rangeStart, String rangeEnd, Integer from, Integer size);

    /**
     * Update event and status (rejection/publication).
     * @param eventAdminRequest Entity dto.
     * @param id Event id.
     * @return EventFullDto.
     */
    EventFullDto update(UpdateEventAdminRequest eventAdminRequest, Long id);
}