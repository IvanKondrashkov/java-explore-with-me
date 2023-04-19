package ru.practicum.user.service;

import java.util.List;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.NewUserRequest;

public interface UserServiceAdminApi {
    /**
     * Find all users by ids.
     * @param ids List User id.
     * @param from Number of users that need to be skipped to form the current set. Default value : 0.
     * @param size Number of users in the set. Default value : 10.
     * @return List UserDto.
     */
    List<UserDto> findAll(List<Long> ids, Integer from, Integer size);

    /**
     * Create user.
     * @param userRequest Entity dto.
     * @return UserDto.
     */
    UserDto save(NewUserRequest userRequest);

    /**
     * Delete user by id.
     * @param id User id.
     */
    void deleteById(Long id);
}