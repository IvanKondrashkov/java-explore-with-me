package ru.practicum.service;

import java.util.Map;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import ru.practicum.dto.ViewStats;
import ru.practicum.dto.EndpointHit;
import ru.practicum.model.Stats;
import ru.practicum.repo.StatsRepo;
import java.util.function.Function;
import java.util.function.Predicate;
import ru.practicum.mapper.StatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepo statsRepo;

    @Override
    public List<ViewStats> findStats(String start, String end, List<String> uris, Boolean unique) {
        if (uris != null) {
            if (unique) {
                Map<String, Integer> uniqueHits = uris.stream()
                        .collect(Collectors.toMap(uri -> uri, statsRepo::countDistinctIp, (a, b) -> b));
                return findStatsByHits(start, end, uris, uniqueHits);
            } else {
                Map<String, Integer> hits = uris.stream()
                        .collect(Collectors.toMap(uri -> uri, statsRepo::countIp, (a, b) -> b));
                return findStatsByHits(start, end, uris, hits);
            }
        }
        final List<String> allUris = statsRepo.findAll().stream()
                .map(Stats::getUri)
                .collect(Collectors.toList());
        final Map<String, Integer> allHits = allUris.stream()
                .collect(Collectors.toMap(uri -> uri, statsRepo::countIp, (a, b) -> b));
        return findStatsByHits(start, end, allUris, allHits);
    }

    @Override
    @Transactional
    public EndpointHit save(EndpointHit endpointHit) {
        final Stats stats = StatsMapper.toStats(endpointHit);
        final Stats statsWrap = statsRepo.save(stats);
        return StatsMapper.toEndpointHit(statsWrap);
    }

    private List<ViewStats> findStatsByHits(String start, String end, List<String> uris, Map<String, Integer> hits) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return statsRepo.findAllByUriInAndTimestampBetween(uris, LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter))
                .stream()
                .filter(distinctByKey(Stats::getUri))
                .map(it -> StatsMapper.toViewStats(it, hits.get(it.getUri())))
                .sorted(Comparator.comparing(ViewStats::getHits).reversed())
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}