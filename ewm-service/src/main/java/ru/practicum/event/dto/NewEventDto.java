package ru.practicum.event.dto;

import lombok.*;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @Size(max = 120, min = 3)
    @NotNull
    private String title;
    @Size(max = 2000, min = 20)
    @NotNull
    private String annotation;
    @Size(max = 7000, min = 20)
    @NotNull
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private String eventDate;
    @NotNull
    private Boolean paid;
    @NotNull
    private Boolean requestModeration;
    @NotNull
    private Integer participantLimit;
    @NotNull
    private LocationDto location;
    @NotNull
    private Long category;
}