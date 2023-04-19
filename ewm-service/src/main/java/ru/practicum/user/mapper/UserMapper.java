package ru.practicum.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.NewUserRequest;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UserMapper {
    public static User toUser(NewUserRequest userRequest) {
        return new User(
                null,
                userRequest.getName(),
                userRequest.getEmail()
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}