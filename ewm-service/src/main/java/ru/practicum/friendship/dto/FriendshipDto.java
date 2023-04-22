package ru.practicum.friendship.dto;

import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.user.dto.UserShortDto;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipDto {
    private Long id;
    private Boolean status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private UserShortDto initiator;
    private UserShortDto friend;
}