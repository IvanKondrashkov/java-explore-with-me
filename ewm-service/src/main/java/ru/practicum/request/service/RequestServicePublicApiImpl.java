package ru.practicum.request.service;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.model.User;
import ru.practicum.user.repo.UserRepo;
import ru.practicum.request.model.Status;
import ru.practicum.request.repo.RequestRepo;
import ru.practicum.event.model.Event;
import ru.practicum.event.repo.EventRepo;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.views.service.ViewsService;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServicePublicApiImpl implements RequestServicePublicApi {
    private final UserRepo userRepo;
    private final RequestRepo requestRepo;
    private final EventRepo eventRepo;
    private final ViewsService viewsService;

    @Override
    public List<EventFullDto> findAll(Long userId, List<Long> ids) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Set<Event> events = eventRepo.findAllByIdIn(ids);
        final Map<Long, Integer> views = viewsService.getViews(ids);
        final Map<Long, Integer> confirmedRequests = events.stream()
                .collect(Collectors.toMap(Event::getId, it -> requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, it.getId()), (a, b) -> b));
        return events.stream()
                .map(it -> EventMapper.toEventFullDto(it, views.get(it.getId()), confirmedRequests.get(it.getId())))
                .collect(Collectors.toList());
    }
}