package ru.practicum.event.service;

import java.util.*;
import java.time.LocalDateTime;
import ru.practicum.event.model.*;
import java.util.stream.Collectors;
import com.querydsl.core.BooleanBuilder;
import ru.practicum.request.model.QRequest;
import com.querydsl.core.types.dsl.NumberExpression;
import ru.practicum.request.model.Status;
import ru.practicum.request.repo.RequestRepo;
import ru.practicum.event.repo.EventRepo;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.dto.EventFullDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.views.service.ViewsService;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServicePublicApiImpl implements EventServicePublicApi {
    private final RequestRepo requestRepo;
    private final EventRepo eventRepo;
    private final ViewsService viewsService;

    @Override
    public EventFullDto findById(Long id) {
        final Event eventWrap = eventRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d was not found", id))
        );
        if (eventWrap.getState() != State.PUBLISHED) {
            throw new ConflictException(String.format("The state must have a state=%s", State.PUBLISHED.name()));
        }
        final Integer views = viewsService.getViews(eventWrap.getId());
        final Integer confirmedRequests = requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, eventWrap.getId());
        return EventMapper.toEventFullDto(eventWrap, views, confirmedRequests);
    }

    @Override
    public List<EventFullDto> findAll(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                                         Boolean onlyAvailable, String sort, Integer from, Integer size) {
        final QEvent event = QEvent.event;
        final QRequest request = QRequest.request;
        final PageRequest pageRequest = PageRequest.of((from / size), size);
        final BooleanBuilder conditions = new BooleanBuilder();
        if (text != null && !text.isBlank()) {
            conditions.and(event.annotation.likeIgnoreCase(text).or(event.description.likeIgnoreCase(text)));
        }
        if (categories != null && !categories.isEmpty()) {
            conditions.and(event.category.id.in(categories));
        }
        if (paid != null) {
            conditions.and(event.paid.eq(paid));
        }
        if ((rangeStart != null && !rangeStart.isEmpty()) && (rangeEnd != null && !rangeEnd.isEmpty())) {
            final LocalDateTime start = LocalDateTime.parse(rangeStart, EventMapper.FORMATTER);
            final LocalDateTime end = LocalDateTime.parse(rangeEnd, EventMapper.FORMATTER);
            conditions.and(event.eventDate.between(start, end));
        } else {
            conditions.and(event.eventDate.after(LocalDateTime.now()));
        }
        if (onlyAvailable != null && onlyAvailable) {
            final NumberExpression<Long> participantLimit = request.event.id.eq(event.id).and(request.status.eq(Status.CONFIRMED)).count();
            conditions.and(participantLimit.loe(event.participantLimit));
        }
        if (sort != null) {
            if (sort.equals(Sorting.VIEWS.name())) {
                pageRequest.withSort(Sort.by(Sort.Direction.DESC, "views"));
            }
            if (sort.equals(Sorting.EVENT_DATE.name())) {
                pageRequest.withSort(Sort.by(Sort.Direction.DESC, "eventDate"));
            }
        }
        final List<Event> events = eventRepo.findAll(conditions, pageRequest).getContent();
        final List<Long> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        final Map<Long, Integer> views = viewsService.getViews(ids);
        final Map<Long, Integer> confirmedRequests = events.stream()
                .collect(Collectors.toMap(Event::getId, it -> requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, it.getId()), (a, b) -> b));
        return events.stream()
                .map(it -> EventMapper.toEventFullDto(it, views.get(it.getId()), confirmedRequests.get(it.getId())))
                .collect(Collectors.toList());
    }
}