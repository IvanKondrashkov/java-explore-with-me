package ru.practicum.private_api.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventServicePrivateApi;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventPrivateApiController {
    private final EventServicePrivateApi eventService;

    @GetMapping("/{userId}/events/{id}")
    public EventFullDto findById(@Positive @PathVariable Long userId, @Positive @PathVariable Long id) {
        log.info("Send get request /users/{}/events/{}", userId, id);
        return eventService.findById(userId, id);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> findAll(@Positive @PathVariable Long userId,
                                       @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                       @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Send get request /users/{}/events", userId);
        return eventService.findAll(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@Valid @RequestBody NewEventDto newEventDto, @Positive @PathVariable Long userId) {
        log.info("Send post request /users/{}/events", userId);
        return eventService.save(newEventDto, userId);
    }

    @PatchMapping("/{userId}/events/{id}")
    public EventFullDto update(@RequestBody UpdateEventUserRequest eventUserRequest,
                               @Positive @PathVariable Long userId,
                               @Positive @PathVariable Long id) {
        log.info("Send patch request /users/{}/events/{}", userId, id);
        return eventService.update(eventUserRequest, userId, id);
    }
}