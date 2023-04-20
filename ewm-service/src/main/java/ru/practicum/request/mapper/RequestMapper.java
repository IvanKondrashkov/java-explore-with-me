package ru.practicum.request.mapper;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import ru.practicum.user.model.User;
import ru.practicum.request.model.Status;
import ru.practicum.request.model.Request;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class RequestMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                ofStringByPattern(request.getCreated()),
                request.getStatus().name(),
                request.getRequester().getId(),
                request.getEvent().getId()
        );
    }

    public static Request toRequest(User requester, Event event) {
        return new Request(
                null,
                LocalDateTime.now(),
                event.getRequestModeration() ? Status.PENDING : Status.CONFIRMED,
                requester,
                event
        );
    }

    public static EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<Request> requests) {
        final List<Request> confirmed = requests.stream()
                .filter(it -> it.getStatus() == Status.CONFIRMED)
                .collect(Collectors.toList());
        final List<Request> rejected = requests.stream()
                .filter(it -> it.getStatus() == Status.REJECTED)
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(
                confirmed.stream()
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList()),
                rejected.stream()
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList())

        );
    }

    private static String ofStringByPattern(LocalDateTime date) {
        return date.format(FORMATTER);
    }
}