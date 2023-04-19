package ru.practicum.views.service;

import java.util.Map;
import java.util.List;

public interface ViewsService {
    /**
     *
     * @param id
     * @return
     */
    Integer getViews(Long id);

    /**
     *
     * @param ids
     * @return
     */
    Map<Long, Integer> getViews(List<Long> ids);
}