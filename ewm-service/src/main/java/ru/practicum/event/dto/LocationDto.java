package ru.practicum.event.dto;

import lombok.*;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
}