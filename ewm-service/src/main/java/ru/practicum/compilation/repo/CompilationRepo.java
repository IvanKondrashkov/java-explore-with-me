package ru.practicum.compilation.repo;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.practicum.compilation.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationRepo extends JpaRepository<Compilation, Long> {
    List<Compilation> findAllByPinnedEquals(Boolean pinned, Pageable pageable);
}