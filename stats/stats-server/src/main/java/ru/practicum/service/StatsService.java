package ru.practicum.service;

import java.util.List;
import ru.practicum.dto.ViewStats;
import ru.practicum.dto.EndpointHit;

public interface StatsService {
    /**
     * Find all stats by between interval (start, end) and unique ip request to uri.
     * @param uris List uri.
     * @param unique Unique ip request to uri, parameter can take true or false. Default value = false.
     * @return List view stats.
     */
    List<ViewStats> findStats(String start, String end, List<String> uris, Boolean unique);

    /**
     * Create stats.
     * @param endpointHit Entity dto.
     * @return EndpointHit.
     */
    EndpointHit save(EndpointHit endpointHit);
}
