package ru.practicum.event.dto;

import lombok.*;
import java.util.List;
import javax.validation.constraints.NotNull;
import ru.practicum.request.dto.ParticipationRequestDto;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    @NotNull
    private List<ParticipationRequestDto> confirmedRequests;
    @NotNull
    private List<ParticipationRequestDto> rejectedRequests;
}