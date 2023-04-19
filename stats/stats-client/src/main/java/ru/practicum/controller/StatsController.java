package ru.practicum.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.dto.ViewStats;
import ru.practicum.dto.EndpointHit;
import ru.practicum.client.StatsClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsClient client;

    @GetMapping("/stats")
    public Flux<ViewStats> findStats(@RequestParam(value = "start") String start,
                                    @RequestParam(value = "end") String end,
                                    @RequestParam(value = "uris", required = false) List<String> uris,
                                    @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("Send get request /stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique);
        return client.findStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EndpointHit> save(@Valid @RequestBody EndpointHit endpointHit) {
        log.info("Send post request /hit");
        return client.save(endpointHit);
    }
}