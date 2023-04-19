package ru.practicum.views.service;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import ru.practicum.dto.ViewStats;
import ru.practicum.client.StatsClient;
import ru.practicum.event.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ViewsServiceImpl implements ViewsService {
    private final StatsClient client;

    @Override
    public Integer getViews(Long id) {
        final String start = LocalDateTime.now().minusYears(1).format(EventMapper.FORMATTER);
        final String end = LocalDateTime.now().format(EventMapper.FORMATTER);
        final List<String> uris = List.of(String.format("/events/%d", id));
        final List<ViewStats> viewStats = client.findStats(start, end, uris, false)
                .toStream()
                .collect(Collectors.toList());
        return viewStats.isEmpty() ? 0 : viewStats.get(0).getHits();
    }

    @Override
    public Map<Long, Integer> getViews(List<Long> ids) {
        final String start = LocalDateTime.now().minusYears(1).format(EventMapper.FORMATTER);
        final String end = LocalDateTime.now().format(EventMapper.FORMATTER);
        final List<String> uris = ids.stream()
                .map(id -> String.format("/events/%d", id))
                .collect(Collectors.toList());
        final List<ViewStats> viewStats = client.findStats(start, end, uris, false)
                .toStream()
                .collect(Collectors.toList());
        final Map<Long, Integer> views = ids.stream()
                .collect(Collectors.toMap(id -> id, id -> 0, (a, b) -> b));
        for (ViewStats view : viewStats) {
            final Long id = Long.valueOf(view.getUri().split("/", 3)[2]);
            if (views.containsKey(id)) {
                views.put(id, view.getHits());
            }
        }
        return views;
    }
}