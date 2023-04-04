package ru.practicum.client;

import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.dto.ViewStats;
import ru.practicum.dto.EndpointHit;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class StatsClient {
    @Value("${stats.server.url}")
    private String url;
    private final WebClient client = WebClient.builder()
            .baseUrl(url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public Mono<EndpointHit> save(EndpointHit endpointHit) {
        return client.post()
                .uri("/hit")
                .body(Mono.just(endpointHit), EndpointHit.class)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        log.info("Send post request /hit statusCode={}", response.statusCode());
                        return response.bodyToMono(EndpointHit.class);
                    } else {
                        log.error("Send post request /hit statusCode={}", response.statusCode());
                        return response.createException().flatMap(Mono::error);
                    }
                });
    }

    public Flux<ViewStats> findStats(String start, String end, List<String> uris, Boolean unique) {
        return client.get()
                .uri("/stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique)
                .exchangeToFlux(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        log.info("Send get request /stats?start={}&end={}&uris={}&unique={} statusCode={}",
                                start, end, uris, unique, response.statusCode());
                        return response.bodyToFlux(ViewStats.class);
                    } else {
                        log.error("Send get request /stats?start={}&end={}&uris={}&unique={} statusCode={}",
                                start, end, uris, unique, response.statusCode());
                        return response.createException().flatMapMany(Flux::error);
                    }
                });
    }
}
