package ru.practicum.controller;

import java.util.List;
import ru.practicum.dto.ViewStats;
import ru.practicum.dto.EndpointHit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(value = "start") String start,
                                    @RequestParam(value = "end") String end,
                                    @RequestParam(value = "uris", required = false) List<String> uris,
                                    @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("Send get request /stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit hit(@RequestBody EndpointHit endpointHit) {
        log.info("Send post request /hit");
        return statsService.hit(endpointHit);
    }
}
