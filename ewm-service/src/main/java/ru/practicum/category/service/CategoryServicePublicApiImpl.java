package ru.practicum.category.service;

import java.util.List;
import java.util.stream.Collectors;
import ru.practicum.category.model.Category;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.repo.CategoryRepo;
import ru.practicum.category.mapper.CategoryMapper;
import org.springframework.data.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServicePublicApiImpl implements CategoryServicePublicApi {
    private final CategoryRepo categoryRepo;

    @Override
    public CategoryDto findById(Long id) {
        final Category categoryWrap = categoryRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id=%d was not found", id))
        );
        return CategoryMapper.toCategoryDto(categoryWrap);
    }

    @Override
    public List<CategoryDto> findAll(Integer from, Integer size) {
        return categoryRepo.findAll(PageRequest.of((from / size), size)).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}