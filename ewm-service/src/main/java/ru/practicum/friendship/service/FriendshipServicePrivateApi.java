package ru.practicum.friendship.service;

import java.util.List;
import ru.practicum.friendship.dto.FriendshipDto;
import ru.practicum.friendship.dto.FriendshipEventDto;
import ru.practicum.friendship.dto.UpdateFriendshipRequest;

public interface FriendshipServicePrivateApi {
    /**
     * Find all friends by user id.
     * @param userId User id.
     * @return List FriendshipEventDto.
     */
    List<FriendshipEventDto> findAll(Long userId);

    /**
     * Create friendship.
     * @param userId User id.
     * @param friendId User id.
     * @return FriendshipDto.
     */
    FriendshipDto save(Long userId, Long friendId);

    /**
     * Update friendship.
     * @param updateFriendshipRequest Entity dto.
     * @param userId User id.
     * @param id Friendship id.
     * @return FriendshipDto.
     */
    FriendshipDto update(UpdateFriendshipRequest updateFriendshipRequest, Long userId, Long id);

    /**
     * Delete friendship.
     * @param userId User id.
     * @param id Friendship id.
     */
    void deleteById(Long userId, Long id);
}