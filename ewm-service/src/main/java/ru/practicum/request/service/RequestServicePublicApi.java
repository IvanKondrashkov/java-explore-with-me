package ru.practicum.request.service;

import java.util.List;
import ru.practicum.event.dto.EventFullDto;

public interface RequestServicePublicApi {
    /**
     * Find all events.
     * @param userId User id.
     * @param ids Events ids.
     * @return List EventFullDto.
     */
    List<EventFullDto> findAll(Long userId, List<Long> ids);
}