package ru.practicum.category.service;

import java.util.Optional;
import ru.practicum.category.model.Category;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.repo.CategoryRepo;
import ru.practicum.category.mapper.CategoryMapper;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceAdminApiImpl implements CategoryServiceAdminApi {
    private final CategoryRepo categoryRepo;

    @Override
    @Transactional
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        final Category category = CategoryMapper.toCategory(newCategoryDto);
        final Category categoryWrap = categoryRepo.save(category);
        return CategoryMapper.toCategoryDto(categoryWrap);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto, Long id) {
        final Category categoryWrap = categoryRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id=%d was not found", id))
        );
        Optional.ofNullable(categoryDto.getName()).ifPresent(it -> {
            if (!StringUtils.isBlank(categoryDto.getName())) categoryWrap.setName(categoryDto.getName());
        });
        return CategoryMapper.toCategoryDto(categoryWrap);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        final Category categoryWrap = categoryRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id=%d was not found", id))
        );
        categoryRepo.deleteById(categoryWrap.getId());
    }
}