package ru.practicum.dto;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Integer hits;
}
