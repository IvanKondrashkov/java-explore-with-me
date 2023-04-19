package ru.practicum.request.dto;

import lombok.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String created;
    @NotBlank
    private String status;
    @NotNull
    private Long requester;
    @NotNull
    private Long event;
}