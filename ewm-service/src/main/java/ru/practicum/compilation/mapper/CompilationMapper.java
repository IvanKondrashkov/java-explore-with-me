package ru.practicum.compilation.mapper;

import java.util.Map;
import java.util.Set;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.stream.Collectors;
import ru.practicum.event.model.Event;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import org.apache.commons.collections.CollectionUtils;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        return new Compilation(
                null,
                newCompilationDto.getTitle(),
                newCompilationDto.getPinned(),
                CollectionUtils.isEmpty(events) ? Collections.emptySet() : events
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation, Map<Long, Integer> views, Map<Long, Integer> confirmedRequests) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                compilation.getEvents().stream()
                        .map(it -> EventMapper.toEventShortDto(it, views.get(it.getId()), confirmedRequests.get(it.getId())))
                        .collect(Collectors.toSet())
                );
    }
}