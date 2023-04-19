package ru.practicum.admin.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.service.EventServiceAdminApi;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventServiceAdminApi eventService;

    @GetMapping("/events")
    public List<EventFullDto> findAll(@RequestParam(name = "users", required = false) List<Long> users,
                                      @RequestParam(name = "states", required = false) List<String> states,
                                      @RequestParam(name = "categories", required = false) List<Long> categories,
                                      @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                      @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                      @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                      @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Send get request /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.findAll(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{id}")
    public EventFullDto update(@RequestBody UpdateEventAdminRequest eventAdminRequest, @Positive @PathVariable Long id) {
        log.info("Send patch request /admin/events/{}", id);
        return eventService.update(eventAdminRequest, id);
    }
}