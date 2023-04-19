package ru.practicum.compilation.service;

import java.util.List;
import ru.practicum.compilation.dto.CompilationDto;

public interface CompilationServicePublicApi {
    /**
     * Find compilation by id.
     * @param id Compilation id.
     * @return CompilationDto.
     */
    CompilationDto findById(Long id);

    /**
     * Find all compilations.
     * @param pinned Only pinned/not pinned compilations.
     * @param from Number of compilations that need to be skipped to form the current set. Default value : 0.
     * @param size Number of compilations in the set. Default value : 10.
     * @return List CompilationDto.
     */
    List<CompilationDto> findAll(Boolean pinned, Integer from, Integer size);
}