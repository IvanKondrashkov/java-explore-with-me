package ru.practicum.user.repo;

import java.util.List;
import ru.practicum.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findAllByIdIn(List<Long> ids);
}