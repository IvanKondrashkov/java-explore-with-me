package ru.practicum.repo;

import java.util.List;
import java.time.LocalDateTime;
import ru.practicum.model.Stats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepo extends JpaRepository<Stats, Long> {
    List<Stats> findAllByUriInAndTimestampBetween(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select count(s.ip) from Stats as s where s.uri = ?1")
    Integer countIp(String uri);

    @Query("select count(distinct s.ip) from Stats as s where s.uri = ?1")
    Integer countDistinctIp(String uri);
}