package ru.practicum.compilation.dto;

import lombok.*;
import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    private String title;
    private Boolean pinned;
    private List<Long> events;
}