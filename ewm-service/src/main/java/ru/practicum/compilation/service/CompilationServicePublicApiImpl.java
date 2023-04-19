package ru.practicum.compilation.service;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import ru.practicum.request.model.Status;
import ru.practicum.request.repo.RequestRepo;
import ru.practicum.event.model.Event;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.repo.CompilationRepo;
import ru.practicum.compilation.mapper.CompilationMapper;
import org.springframework.data.domain.PageRequest;
import ru.practicum.views.service.ViewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServicePublicApiImpl implements CompilationServicePublicApi {
    private final RequestRepo requestRepo;
    private final CompilationRepo compilationRepo;
    private final ViewsService viewsService;

    @Override
    public CompilationDto findById(Long id) {
        final Compilation compilationWrap = compilationRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Compilation with id=%d was not found", id))
        );
        final Set<Event> events = compilationWrap.getEvents();
        final List<Long> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        final Map<Long, Integer> views = viewsService.getViews(ids);
        final Map<Long, Integer> confirmedRequests = events.stream()
                .collect(Collectors.toMap(Event::getId, it -> requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, it.getId()), (a, b) -> b));
        return CompilationMapper.toCompilationDto(compilationWrap, views, confirmedRequests);
    }

    @Override
    public List<CompilationDto> findAll(Boolean pinned, Integer from, Integer size) {
        final List<Compilation> compilations = compilationRepo.findAllByPinnedEquals(pinned, PageRequest.of((from / size), size));
        final Set<Event> events = compilations.stream()
                .flatMap(it -> it.getEvents().stream())
                .collect(Collectors.toSet());
        final List<Long> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        final Map<Long, Integer> views = viewsService.getViews(ids);
        final Map<Long, Integer> confirmedRequests = events.stream()
                .collect(Collectors.toMap(Event::getId, it -> requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, it.getId()), (a, b) -> b));
        return compilations.stream()
                .map(it -> CompilationMapper.toCompilationDto(it, views, confirmedRequests))
                .collect(Collectors.toList());
    }
}