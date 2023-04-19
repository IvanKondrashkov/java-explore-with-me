package ru.practicum.user.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    private Long id;
    private String name;
}