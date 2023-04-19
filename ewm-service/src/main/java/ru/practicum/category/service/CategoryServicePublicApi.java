package ru.practicum.category.service;

import java.util.List;
import ru.practicum.category.dto.CategoryDto;

public interface CategoryServicePublicApi {
    /**
     * Find category by id.
     * @param id Category id.
     * @return CategoryDto.
     */
    CategoryDto findById(Long id);

    /**
     * Find all categories.
     * @param from Number of categories that need to be skipped to form the current set. Default value : 0.
     * @param size Number of categories in the set. Default value : 10.
     * @return List CategoryDto.
     */
    List<CategoryDto> findAll(Integer from, Integer size);
}