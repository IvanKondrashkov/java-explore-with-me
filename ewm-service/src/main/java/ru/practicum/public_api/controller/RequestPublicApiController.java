package ru.practicum.public_api.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import ru.practicum.event.dto.EventFullDto;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.service.RequestServicePublicApi;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestPublicApiController {
    private final RequestServicePublicApi requestService;

    @GetMapping("/{userId}/friends/requests")
    public List<EventFullDto> findAll(@Positive @PathVariable Long userId, @RequestParam List<Long> ids) {
        log.info("Send get request /users/{}/requests?ids={}", userId, ids);
        return requestService.findAll(userId, ids);
    }
}