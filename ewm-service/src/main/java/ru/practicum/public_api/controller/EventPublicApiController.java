package ru.practicum.public_api.controller;

import java.util.List;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHit;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.EventServicePublicApi;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class EventPublicApiController {
    private final StatsClient client;
    private final EventServicePublicApi eventService;

    @GetMapping("/events/{id}")
    public EventFullDto findById(@Positive @PathVariable Long id, HttpServletRequest request) {
        log.info("Send get request /events/{}", id);
        client.save(new EndpointHit(null, "ewn-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now())).subscribe();
        return eventService.findById(id);
    }

    @GetMapping("/events")
    public List<EventFullDto> findAll(@RequestParam(name = "text", required = false) String text,
                                      @RequestParam(name = "categories", required = false) List<Long> categories,
                                      @RequestParam(name = "paid", required = false) Boolean paid,
                                      @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                      @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                      @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(name = "sort", required = false) String sort,
                                      @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                      @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        log.info("Send get request /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort={}&from={}&size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        client.save(new EndpointHit(null, "ewn-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now())).subscribe();
        return eventService.findAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
}