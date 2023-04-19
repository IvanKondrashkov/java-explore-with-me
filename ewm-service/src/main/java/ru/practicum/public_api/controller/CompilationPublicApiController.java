package ru.practicum.public_api.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationServicePublicApi;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CompilationPublicApiController {
    private final CompilationServicePublicApi compilationService;

    @GetMapping("/compilations/{id}")
    public CompilationDto findById(@Positive @PathVariable Long id) {
        log.info("Send get request /compilations/{}", id);
        return compilationService.findById(id);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> findAll(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                        @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                        @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Send get request /compilations?pinned={}&from={}&size={}", pinned, from, size);
        return compilationService.findAll(pinned, from, size);
    }
}