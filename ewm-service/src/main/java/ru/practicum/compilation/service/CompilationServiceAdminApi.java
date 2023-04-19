package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

public interface CompilationServiceAdminApi {
    /**
     * Create compilation.
     * @param newCompilationDto Entity dto.
     * @return CompilationDto.
     */
    CompilationDto save(NewCompilationDto newCompilationDto);

    /**
     * Update compilation by id.
     * @param compilationRequest Entity dto.
     * @param id Compilation id.
     * @return CompilationDto.
     */
    CompilationDto update(UpdateCompilationRequest compilationRequest, Long id);

    /**
     * Delete compilation by id.
     * @param id Compilation id.
     */
    void deleteById(Long id);
}