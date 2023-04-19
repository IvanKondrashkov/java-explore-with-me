package ru.practicum.private_api.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestServicePrivateApi;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestPrivateApiController {
    private final RequestServicePrivateApi requestService;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> findAllByUserId(@Positive @PathVariable Long userId) {
        log.info("Send get request /users/{}/requests", userId);
        return requestService.findAllByUserId(userId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findAllByEventIdAndEventInitiatorId(@Positive @PathVariable Long eventId,
                                                                             @Positive @PathVariable Long userId) {
        log.info("Send get request /users/{}/events/{}/requests", userId, eventId);
        return requestService.findAllByEventIdAndEventInitiatorId(eventId, userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto save(@Positive @PathVariable Long userId, @Positive @RequestParam Long eventId) {
        log.info("Send post request /users/{}/requests?eventId={}", userId, eventId);
        return requestService.save(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{id}/cancel")
    public ParticipationRequestDto cancelById(@Positive @PathVariable Long userId, @Positive @PathVariable Long id) {
        log.info("Send patch request /users/{}/requests/{}/cancel", userId, id);
        return requestService.cancelById(userId, id);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatus(@Valid @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest,
                                                       @Positive @PathVariable Long userId,
                                                       @Positive @PathVariable Long eventId) {
        log.info("Send patch request /users/{}/events/{}/requests", userId, eventId);
        return requestService.updateStatus(statusUpdateRequest, userId, eventId);
    }
}