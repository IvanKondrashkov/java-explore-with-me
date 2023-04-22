package ru.practicum.request.repo;

import java.util.List;
import java.util.Optional;
import ru.practicum.request.model.Status;
import ru.practicum.request.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepo extends JpaRepository<Request, Long> {
    List<Request> findAllByIdIn(List<Long> ids);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByRequesterIdAndStatusEquals(Long userId, Status status);

    List<Request> findAllByEventIdAndEvent_InitiatorId(Long eventId, Long userId);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    Integer countAllByStatusEqualsAndEvent_Id(Status status, Long eventId);
}