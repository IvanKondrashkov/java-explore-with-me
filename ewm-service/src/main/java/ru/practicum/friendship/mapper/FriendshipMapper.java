package ru.practicum.friendship.mapper;

import java.util.List;
import lombok.AccessLevel;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import ru.practicum.user.model.User;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.friendship.model.Friendship;
import ru.practicum.friendship.dto.FriendshipDto;
import ru.practicum.friendship.dto.FriendshipEventDto;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class FriendshipMapper {
    public static Friendship toFriendship(User initiator, User friend) {
        return new Friendship(
                null,
                false,
                LocalDateTime.now(),
                initiator,
                friend
        );
    }

    public static FriendshipDto toFriendshipDto(Friendship friendship) {
        return new FriendshipDto(
                friendship.getId(),
                friendship.getIsFriend(),
                LocalDateTime.now(),
                UserMapper.toUserShortDto(friendship.getInitiator()),
                UserMapper.toUserShortDto(friendship.getFriend())
        );
    }

    public static FriendshipEventDto toFriendshipEventDto(User friend, List<EventFullDto> events) {
        return new FriendshipEventDto(
                UserMapper.toUserShortDto(friend),
                events
        );
    }
}