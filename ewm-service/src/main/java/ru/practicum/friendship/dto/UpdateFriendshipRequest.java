package ru.practicum.friendship.dto;

import lombok.*;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFriendshipRequest {
    @NotNull
    private Boolean status;
}