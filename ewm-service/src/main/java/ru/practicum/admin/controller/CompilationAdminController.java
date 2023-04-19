package ru.practicum.admin.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationServiceAdminApi;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationServiceAdminApi compilationService;

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto save(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Send post request /admin/compilations");
        return compilationService.save(newCompilationDto);
    }

    @PatchMapping("/compilations/{id}")
    public CompilationDto update(@RequestBody UpdateCompilationRequest compilationRequest, @Positive @PathVariable Long id) {
        log.info("Send patch request /admin/compilations/{}", id);
        return compilationService.update(compilationRequest, id);
    }

    @DeleteMapping("/compilations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Positive @PathVariable Long id) {
        log.info("Send delete request /admin/compilations/{}", id);
        compilationService.deleteById(id);
    }
}