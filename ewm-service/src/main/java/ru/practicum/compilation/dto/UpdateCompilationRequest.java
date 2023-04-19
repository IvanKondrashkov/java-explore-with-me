package ru.practicum.compilation.dto;

import lombok.*;
import java.util.Set;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    private String title;
    private Boolean pinned;
    private Set<Long> events;
}