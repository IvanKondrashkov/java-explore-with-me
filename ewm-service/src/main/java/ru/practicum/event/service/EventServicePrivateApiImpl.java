package ru.practicum.event.service;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.user.model.User;
import ru.practicum.user.repo.UserRepo;
import ru.practicum.category.model.Category;
import ru.practicum.category.repo.CategoryRepo;
import ru.practicum.request.model.Status;
import ru.practicum.request.repo.RequestRepo;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.State;
import ru.practicum.event.model.StateAction;
import ru.practicum.event.repo.EventRepo;
import ru.practicum.event.repo.LocationRepo;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.mapper.LocationMapper;
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
public class EventServicePrivateApiImpl implements EventServicePrivateApi {
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final RequestRepo requestRepo;
    private final EventRepo eventRepo;
    private final LocationRepo locationRepo;
    private final ViewsService viewsService;

    @Override
    public EventFullDto findById(Long userId, Long id) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Event eventWrap = eventRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d was not found", id))
        );
        final Event event = eventRepo.findByInitiatorIdAndId(userWrap.getId(), eventWrap.getId());
        final Integer views = viewsService.getViews(eventWrap.getId());
        final Integer confirmedRequests = requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, eventWrap.getId());
        return EventMapper.toEventFullDto(event, views, confirmedRequests);
    }

    @Override
    public List<EventShortDto> findAll(Long userId, Integer from, Integer size) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final List<Event> events = eventRepo.findAllByInitiatorId(userWrap.getId(), PageRequest.of((from / size), size));
        final List<Long> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        final Map<Long, Integer> views = viewsService.getViews(ids);
        final Map<Long, Integer> confirmedRequests = events.stream()
                .collect(Collectors.toMap(Event::getId, it -> requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, it.getId()), (a, b) -> b));
        return events.stream()
                .map(it -> EventMapper.toEventShortDto(it, views.get(it.getId()), confirmedRequests.get(it.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto save(NewEventDto newEventDto, Long userId) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Category categoryWrap = categoryRepo.findById(newEventDto.getCategory()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id=%d was not found", newEventDto.getCategory()))
        );
        final Location location = LocationMapper.toLocation(newEventDto.getLocation());
        final Location locationWrap = locationRepo.save(location);
        final Event event = EventMapper.toEvent(newEventDto, userWrap, categoryWrap, locationWrap);
        if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException(String.format("Event date is not valid! eventDate=%s", event.getEventDate()));
        }
        final Event eventWrap = eventRepo.save(event);
        return EventMapper.toEventFullDto(eventWrap, 0, 0);
    }

    @Override
    @Transactional
    public EventFullDto update(UpdateEventUserRequest eventUserRequest, Long userId, Long id) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Event eventWrap = eventRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d was not found", id))
        );
        Optional.ofNullable(eventUserRequest.getTitle()).ifPresent(it -> {
            if (!StringUtils.isBlank(eventUserRequest.getTitle())) eventWrap.setTitle(eventUserRequest.getTitle());
        });
        Optional.ofNullable(eventUserRequest.getAnnotation()).ifPresent(it -> {
            if (!StringUtils.isBlank(eventUserRequest.getAnnotation())) eventWrap.setAnnotation(eventUserRequest.getAnnotation());
        });
        Optional.ofNullable(eventUserRequest.getDescription()).ifPresent(it -> {
            if (!StringUtils.isBlank(eventUserRequest.getDescription())) eventWrap.setDescription(eventUserRequest.getDescription());
        });
        Optional.ofNullable(eventUserRequest.getPaid()).ifPresent(eventWrap::setPaid);
        Optional.ofNullable(eventUserRequest.getParticipantLimit()).ifPresent(eventWrap::setParticipantLimit);
        Optional.ofNullable(eventUserRequest.getEventDate()).ifPresent(it -> {
            final LocalDateTime eventDate = LocalDateTime.parse(eventUserRequest.getEventDate(), EventMapper.FORMATTER);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ConflictException(String.format("Event date is not valid! eventDate=%s", eventUserRequest.getEventDate()));
            }
            eventWrap.setEventDate(eventDate);
        });
        Optional.ofNullable(eventUserRequest.getCategory()).ifPresent(it -> {
            final Category categoryWrap = categoryRepo.findById(eventUserRequest.getCategory()).orElseThrow(
                    () -> new EntityNotFoundException(String.format("Category with id=%d was not found", eventUserRequest.getCategory()))
            );
            eventWrap.setCategory(categoryWrap);
        });
        Optional.ofNullable(eventUserRequest.getLocation()).ifPresent(it -> {
            final Location location = LocationMapper.toLocation(eventUserRequest.getLocation());
            final Location locationWrap = locationRepo.save(location);
            eventWrap.setLocation(locationWrap);
        });
        if (eventWrap.getState() == State.PUBLISHED) {
            throw new ConflictException(String.format("The event has already been published! Event state=%s", eventWrap.getState()));
        }
        if (eventUserRequest.getStateAction() != null) {
            final StateAction stateAction = StateAction.valueOf(eventUserRequest.getStateAction());
            switch (stateAction) {
                case SEND_TO_REVIEW: {
                    eventWrap.setState(State.PENDING);
                    break;
                }
                case CANCEL_REVIEW: {
                    eventWrap.setState(State.CANCELED);
                    break;
                }
                default: throw new EntityNotFoundException(String.format("Incorrect stateAction=%s", stateAction));
            }
        }
        final Integer views = viewsService.getViews(eventWrap.getId());
        final Integer confirmedRequests = requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, eventWrap.getId());
        return EventMapper.toEventFullDto(eventWrap, views, confirmedRequests);
    }
}