package ru.practicum.friendship.dto;

import lombok.*;
import java.util.List;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.user.dto.UserShortDto;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipEventDto {
    private UserShortDto friend;
    private List<EventFullDto> events;
}