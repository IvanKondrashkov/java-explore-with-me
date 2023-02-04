package ru.practicum.service;

import java.util.List;
import ru.practicum.dto.ViewStats;
import ru.practicum.dto.EndpointHit;

public interface StatsService {
    /**
     *
     * @param start
     * @param end
     * @param uris
     * @param unique
     * @return
     */
    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);

    /**
     *
     * @param endpointHit
     * @return
     */
    EndpointHit hit(EndpointHit endpointHit);
}
