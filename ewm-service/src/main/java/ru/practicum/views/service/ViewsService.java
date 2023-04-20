package ru.practicum.views.service;

import java.util.Map;
import java.util.List;

public interface ViewsService {
    /**
     * Find view by event id.
     * @param id Event id.
     * @return Count hits.
     */
    Integer getViews(Long id);

    /**
     * Find all views by events ids.
     * @param ids List Event ids.
     * @return Map, key = event id, value = count hits.
     */
    Map<Long, Integer> getViews(List<Long> ids);
}