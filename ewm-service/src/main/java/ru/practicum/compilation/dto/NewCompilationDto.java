package ru.practicum.compilation.dto;

import lombok.*;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @NotBlank
    private String title;
    @NotNull
    private Boolean pinned;
    private Set<Long> events;
}