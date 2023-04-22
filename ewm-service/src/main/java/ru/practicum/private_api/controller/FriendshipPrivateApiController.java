package ru.practicum.private_api.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.friendship.dto.FriendshipDto;
import ru.practicum.friendship.dto.FriendshipEventDto;
import ru.practicum.friendship.dto.UpdateFriendshipRequest;
import ru.practicum.friendship.service.FriendshipServicePrivateApi;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FriendshipPrivateApiController {
    private final FriendshipServicePrivateApi friendshipService;

    @GetMapping("/{userId}/friends")
    public List<FriendshipEventDto> findAll(@Positive @PathVariable Long userId) {
        log.info("Send get request /users/{}/friends", userId);
        return friendshipService.findAll(userId);
    }

    @PostMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FriendshipDto save(@Positive @PathVariable Long userId, @Positive @PathVariable Long friendId) {
        log.info("Send post request /users/{}/friends/{}", userId, friendId);
        return friendshipService.save(userId, friendId);
    }

    @PatchMapping("/{userId}/friends/{id}")
    public FriendshipDto update(@Valid @RequestBody UpdateFriendshipRequest updateFriendshipRequest,
                                @Positive @PathVariable Long userId,
                                @Positive @PathVariable Long id) {
        log.info("Send patch request /users/{}/friends/{}", userId, id);
        return friendshipService.update(updateFriendshipRequest, userId, id);
    }

    @DeleteMapping("/{userId}/friends/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Positive @PathVariable Long userId, @Positive @PathVariable Long id) {
        log.info("Send delete request /users/{}/friends/{}", userId, id);
        friendshipService.deleteById(userId, id);
    }
}