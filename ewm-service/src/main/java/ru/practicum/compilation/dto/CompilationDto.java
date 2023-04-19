package ru.practicum.compilation.dto;

import lombok.*;
import java.util.Set;
import ru.practicum.event.dto.EventShortDto;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private Set<EventShortDto> events;
}