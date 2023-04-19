package ru.practicum.request.service;

import java.util.List;
import java.util.stream.Collectors;
import ru.practicum.event.model.State;
import ru.practicum.user.model.User;
import ru.practicum.user.repo.UserRepo;
import ru.practicum.request.model.Status;
import ru.practicum.request.model.Request;
import ru.practicum.request.repo.RequestRepo;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repo.EventRepo;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import ru.practicum.exception.ConflictException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServicePrivateApiImpl implements RequestServicePrivateApi {
    private final UserRepo userRepo;
    private final RequestRepo requestRepo;
    private final EventRepo eventRepo;

    @Override
    public List<ParticipationRequestDto> findAllByUserId(Long userId) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        return requestRepo.findAllByRequesterId(userWrap.getId()).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Event eventWrap = eventRepo.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d was not found", eventId))
        );
        return requestRepo.findAllByEventIdAndEvent_InitiatorId(eventWrap.getId(), userWrap.getId()).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto save(Long userId, Long eventId) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Event eventWrap = eventRepo.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d was not found", eventId))
        );
        if (requestRepo.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException(String.format("Repeated request for participation is not allowed, userId=%d and eventId=%d", userId, eventId));
        }
        if (eventWrap.getInitiator().getId().equals(userId)) {
            throw new ConflictException(String.format("The initiator of the event cannot send a request to participate, userId=%d", userId));
        }
        if (eventWrap.getState() != State.PUBLISHED) {
            throw new ConflictException(String.format("The state must have a state=%s", State.PUBLISHED));
        }
        final Integer participantLimit = requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, eventWrap.getId());
        if (eventWrap.getParticipantLimit() <= participantLimit) {
            throw new ConflictException(String.format("Maximum number of participants %d", participantLimit));
        }
        final Request request = RequestMapper.toRequest(userWrap, eventWrap);
        final Request requestWrap = requestRepo.save(request);
        return RequestMapper.toParticipationRequestDto(requestWrap);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelById(Long userId, Long id) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Request requestWrap = requestRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Request with id=%d was not found", id))
        );
        requestWrap.setStatus(Status.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestWrap);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatus(EventRequestStatusUpdateRequest statusUpdateRequest, Long userId, Long eventId) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Event eventWrap = eventRepo.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%d was not found", eventId))
        );
        final Status status = Status.valueOf(statusUpdateRequest.getStatus());
        final List<Long> ids = statusUpdateRequest.getRequestIds();
        final List<Request> requests = requestRepo.findAllByIdIn(ids);
        if (requests.stream().allMatch(it -> it.getStatus().equals(Status.PENDING))) {
            for (Request request : requests) {
                final Integer participantLimit = requestRepo.countAllByStatusEqualsAndEvent_Id(Status.CONFIRMED, eventWrap.getId());
                if (eventWrap.getParticipantLimit() == 0 || !eventWrap.getRequestModeration()) {
                    request.setStatus(Status.CONFIRMED);
                }
                if (eventWrap.getParticipantLimit() > participantLimit) {
                    if (status == Status.CONFIRMED) {
                        request.setStatus(Status.CONFIRMED);
                    }
                    if (status == Status.REJECTED) {
                        request.setStatus(Status.REJECTED);
                    }
                } else {
                    throw new ConflictException(String.format("Maximum number of participants %d", participantLimit));
                }
            }
        } else {
            throw new ConflictException(String.format("The status must have a status=%s", Status.PENDING));
        }
        return RequestMapper.toEventRequestStatusUpdateResult(requests);
    }
}