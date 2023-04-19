package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

public interface CategoryServiceAdminApi {
    /**
     * Create category.
     * @param newCategoryDto Entity dto.
     * @return CategoryDto.
     */
    CategoryDto save(NewCategoryDto newCategoryDto);

    /**
     * Update category by id.
     * @param categoryDto Entity dto.
     * @param id Category id.
     * @return CategoryDto.
     */
    CategoryDto update(CategoryDto categoryDto, Long id);

    /**
     * Delete category by id.
     * @param id Category id.
     */
    void deleteById(Long id);
}