package ru.practicum.friendship.repo;

import java.util.List;
import ru.practicum.friendship.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepo extends JpaRepository<Friendship, Long> {
    List<Friendship> findAllByInitiatorIdAndStatusTrue(Long userId);
}