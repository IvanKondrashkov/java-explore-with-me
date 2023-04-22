package ru.practicum.request.service;

import java.util.List;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;

public interface RequestServicePrivateApi {
    /**
     * Find all requests to participate in the event of the other user.
     * @param userId User id.
     * @return List ParticipationRequestDto.
     */
    List<ParticipationRequestDto> findAllByUserId(Long userId);

    /**
     * Find all requests to participate in the event of the current user.
     * @param eventId Event id.
     * @param userId User id.
     * @return List ParticipationRequestDto.
     */
    List<ParticipationRequestDto> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);

    /**
     * Create request.
     * @param userId User id.
     * @param eventId Event id.
     * @return ParticipationRequestDto.
     */
    ParticipationRequestDto save(Long userId, Long eventId);

    /**
     * Cancellation of your request to participate in the event.
     * @param userId User id.
     * @param id Request id.
     * @return ParticipationRequestDto.
     */
    ParticipationRequestDto cancelById(Long userId, Long id);

    /**
     * Update status (confirmed, canceled) of applications for participation in the event of the current user.
     * @param statusUpdateRequest Entity dto.
     * @param userId User id.
     * @param eventId Event id.
     * @return EventRequestStatusUpdateResult.
     */
    EventRequestStatusUpdateResult update(EventRequestStatusUpdateRequest statusUpdateRequest, Long userId, Long eventId);
}