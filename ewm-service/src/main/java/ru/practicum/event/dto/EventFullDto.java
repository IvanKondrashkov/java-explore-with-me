package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.category.dto.CategoryDto;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;
    private String title;
    private String annotation;
    private String description;
    private String eventDate;
    private String createdOn;
    private String publishedOn;
    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;
    private String state;
    private Integer views;
    private Integer confirmedRequests;
    private LocationDto location;
    private CategoryDto category;
    private UserShortDto initiator;
}