package ru.practicum.event.service;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.category.model.Category;
import ru.practicum.category.repo.CategoryRepo;
import ru.practicum.event.model.*;
import ru.practicum.event.repo.LocationRepo;
import ru.practicum.request.model.Status;
import ru.practicum.request.repo.RequestRepo;
import ru.practicum.event.repo.EventRepo;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.mapper.LocationMapper;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.views.service.ViewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceAdminApiImpl implements EventServiceAdminApi {
    private final CategoryRepo categoryRepo;
    private final RequestRepo requestRepo;
    private final EventRepo eventRepo;
    private final LocationRepo locationRepo;
    private final ViewsService viewsService;

    @Override
    public List<EventFullDto> findAll(List<Long> users, List<String> states, List<Long> categories,
                                         String rangeStart, String rangeEnd, Integer from, Integer size) {
        final QEvent event = QEvent.event;
        final BooleanBuilder conditions = new BooleanBuilder();
        if (users != null && !users.isEmpty()) {
            conditions.and(event.initiator.id.in(users));
        }
        if (states != null && !states.isEmpty()) {
            final List<State> stateEnums = states.stream().map(State::valueOf).collect(Collectors.toList());
            conditions.and(event.state.in(stateEnums));
        }
        if (categories != null && !categories.isEmpty()) {
            conditions.and(event.category.id.in(categories));
        }
        if ((rangeStart != null && !rangeStart.isEmpty()) && (rangeEnd != null && !rangeEnd.isEmpty())) {
            final LocalDateTime start = LocalDateTime.parse(rangeStart, EventMapper.FORMATTER);
            final LocalDateTime end = LocalDateTime.parse(rangeEnd, EventMapper.FORMATTER);
            conditions.and(event.eventDate.between(start, end));
        }
        final List<Event> events = eventRepo.findAll(conditions, PageRequest.of((from / size), size)).getContent();
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

    @Override
    @Transactional
    public EventFullDto update(UpdateEventAdminRequest eventAdminRequest, Long id) {
        final Event eventWrap = eventRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d was not found", id))
        );
        Optional.ofNullable(eventAdminRequest.getTitle()).ifPresent(it -> {
            if (!StringUtils.isBlank(eventAdminRequest.getTitle())) eventWrap.setTitle(eventAdminRequest.getTitle());
        });
        Optional.ofNullable(eventAdminRequest.getAnnotation()).ifPresent(it -> {
            if (!StringUtils.isBlank(eventAdminRequest.getAnnotation())) eventWrap.setAnnotation(eventAdminRequest.getAnnotation());
        });
        Optional.ofNullable(eventAdminRequest.getDescription()).ifPresent(it -> {
            if (!StringUtils.isBlank(eventAdminRequest.getDescription())) eventWrap.setDescription(eventAdminRequest.getDescription());
        });
        Optional.ofNullable(eventAdminRequest.getPaid()).ifPresent(eventWrap::setPaid);
        Optional.ofNullable(eventAdminRequest.getParticipantLimit()).ifPresent(eventWrap::setParticipantLimit);
        Optional.ofNullable(eventAdminRequest.getEventDate()).ifPresent(it -> {
            final LocalDateTime eventDate = LocalDateTime.parse(eventAdminRequest.getEventDate(), EventMapper.FORMATTER);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ConflictException(String.format("Event date is not valid! eventDate=%s", eventAdminRequest.getEventDate()));
            }
            eventWrap.setEventDate(eventDate);
        });
        Optional.ofNullable(eventAdminRequest.getCategory()).ifPresent(it -> {
            final Category categoryWrap = categoryRepo.findById(eventAdminRequest.getCategory()).orElseThrow(
                    () -> new EntityNotFoundException(String.format("Category with id=%d was not found", eventAdminRequest.getCategory()))
            );
            eventWrap.setCategory(categoryWrap);
        });
        Optional.ofNullable(eventAdminRequest.getLocation()).ifPresent(it -> {
            final Location location = LocationMapper.toLocation(eventAdminRequest.getLocation());
            final Location locationWrap = locationRepo.save(location);
            eventWrap.setLocation(locationWrap);
        });
        if (eventWrap.getState() == State.PUBLISHED) {
            throw new ConflictException(String.format("The event has already been published! Event state=%s", eventWrap.getState()));
        }
        if (eventAdminRequest.getStateAction() != null) {
            final StateAction stateAction = StateAction.valueOf(eventAdminRequest.getStateAction());
            switch (stateAction) {
                case PUBLISH_EVENT: {
                    eventWrap.setPublishedOn(LocalDateTime.now());
                    eventWrap.setState(State.PUBLISHED);
                    break;
                }
                case REJECT_EVENT: {
                    eventWrap.setState(State.CANCELED);
                    throw new ConflictException(String.format("You can't publish a canceled event! Event state=%s", eventWrap.getState()));
                }
                default: throw new EntityNotFoundException(String.format("Incorrect event stateAction=%s", eventWrap.getState()));
            }
        }
        final Integer views = viewsService.getViews(eventWrap.getId());
        final Integer confirmedRequests = requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, eventWrap.getId());
        return EventMapper.toEventFullDto(eventWrap, views, confirmedRequests);
    }
}