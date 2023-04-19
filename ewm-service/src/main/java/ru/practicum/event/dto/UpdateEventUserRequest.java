package ru.practicum.event.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    private String title;
    private String annotation;
    private String description;
    private String eventDate;
    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;
    private String stateAction;
    private LocationDto location;
    private Long category;
}