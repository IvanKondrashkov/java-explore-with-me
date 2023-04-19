package ru.practicum.category.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CategoryMapper {
    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                null,
                newCategoryDto.getName()
        );
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}