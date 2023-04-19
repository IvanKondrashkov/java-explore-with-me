package ru.practicum.public_api.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryServicePublicApi;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CategoryPublicApiController {
    private final CategoryServicePublicApi categoryService;

    @GetMapping("/categories/{id}")
    public CategoryDto findById(@Positive @PathVariable Long id) {
        log.info("Send get request /categories/{}", id);
        return categoryService.findById(id);
    }

    @GetMapping("/categories")
    public List<CategoryDto> findAll(@PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                     @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Send get request /categories?from={}&size={}", from, size);
        return categoryService.findAll(from, size);
    }
}