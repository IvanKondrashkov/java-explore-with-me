package ru.practicum.admin.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryServiceAdminApi;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryServiceAdminApi categoryService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Send post request /admin/categories");
        return categoryService.save(newCategoryDto);
    }

    @PatchMapping("/categories/{id}")
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto, @Positive @PathVariable Long id) {
        log.info("Send patch request /admin/categories/{}", id);
        return categoryService.update(categoryDto, id);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Positive @PathVariable Long id) {
        log.info("Send delete request /admin/categories/{}", id);
        categoryService.deleteById(id);
    }
}