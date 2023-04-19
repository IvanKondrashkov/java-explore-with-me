package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.category.dto.CategoryDto;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    private String eventDate;
    private Boolean paid;
    private Integer views;
    private Integer confirmedRequests;
    private CategoryDto category;
    private UserShortDto initiator;
}