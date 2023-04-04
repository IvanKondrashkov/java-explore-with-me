package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.model.Stats;
import ru.practicum.dto.ViewStats;
import ru.practicum.dto.EndpointHit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsMapper {
    public static Stats toStats(EndpointHit endpointHit) {
        return Stats.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    public static EndpointHit toEndpointHit(Stats stats) {
        return EndpointHit.builder()
                .id(stats.getId())
                .app(stats.getApp())
                .uri(stats.getUri())
                .ip(stats.getIp())
                .timestamp(stats.getTimestamp())
                .build();
    }

    public static ViewStats toViewStats(Stats stats, Integer hits) {
        return ViewStats.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(hits)
                .build();
    }
}
