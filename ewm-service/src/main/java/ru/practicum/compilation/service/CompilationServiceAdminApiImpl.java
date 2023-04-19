package ru.practicum.compilation.service;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import ru.practicum.request.model.Status;
import ru.practicum.request.repo.RequestRepo;
import ru.practicum.event.model.Event;
import ru.practicum.event.repo.EventRepo;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.repo.CompilationRepo;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.views.service.ViewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceAdminApiImpl implements CompilationServiceAdminApi {
    private final RequestRepo requestRepo;
    private final EventRepo eventRepo;
    private final CompilationRepo compilationRepo;
    private final ViewsService viewsService;

    @Override
    @Transactional
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        final Set<Event> events = eventRepo.findAllByIdIn(newCompilationDto.getEvents());
        final Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        final Compilation compilationWrap = compilationRepo.save(compilation);
        final List<Long> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        final Map<Long, Integer> views = viewsService.getViews(ids);
        final Map<Long, Integer> confirmedRequests = events.stream()
                .collect(Collectors.toMap(Event::getId, it -> requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, it.getId()), (a, b) -> b));
        return CompilationMapper.toCompilationDto(compilationWrap, views, confirmedRequests);
    }

    @Override
    @Transactional
    public CompilationDto update(UpdateCompilationRequest compilationRequest, Long id) {
        final Compilation compilationWrap = compilationRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Compilation with id=%d was not found", id))
        );
        final Set<Event> events = eventRepo.findAllByIdIn(compilationRequest.getEvents());
        Optional.ofNullable(compilationRequest.getTitle()).ifPresent(it -> {
            if (!compilationRequest.getTitle().isBlank()) compilationWrap.setTitle(compilationRequest.getTitle());
        });
        Optional.ofNullable(compilationRequest.getPinned()).ifPresent(compilationWrap::setPinned);
        Optional.ofNullable(compilationRequest.getEvents()).ifPresent(it -> {
            compilationWrap.setEvents(events);
        });
        final List<Long> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        final Map<Long, Integer> views = viewsService.getViews(ids);
        final Map<Long, Integer> confirmedRequests = events.stream()
                .collect(Collectors.toMap(Event::getId, it -> requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, it.getId()), (a, b) -> b));
        return CompilationMapper.toCompilationDto(compilationWrap, views, confirmedRequests);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        final Compilation compilationWrap = compilationRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Compilation with id=%d was not found", id))
        );
        compilationRepo.deleteById(compilationWrap.getId());
    }
}