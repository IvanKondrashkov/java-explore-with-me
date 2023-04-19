package ru.practicum.event.repo;

import java.util.Set;
import java.util.List;
import ru.practicum.event.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EventRepo extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Event findByInitiatorIdAndId(Long userId, Long id);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Set<Event> findAllByIdIn(Set<Long> ids);
}