package ru.practicum.friendship.service;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.model.User;
import ru.practicum.user.repo.UserRepo;
import ru.practicum.event.model.Event;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.request.model.Status;
import ru.practicum.request.model.Request;
import ru.practicum.request.repo.RequestRepo;
import ru.practicum.request.service.RequestServicePublicApi;
import ru.practicum.friendship.model.Friendship;
import ru.practicum.friendship.repo.FriendshipRepo;
import ru.practicum.friendship.mapper.FriendshipMapper;
import ru.practicum.friendship.dto.FriendshipDto;
import ru.practicum.friendship.dto.FriendshipEventDto;
import ru.practicum.friendship.dto.UpdateFriendshipRequest;
import ru.practicum.exception.ConflictException;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendshipServicePrivateApiImpl implements FriendshipServicePrivateApi {
    private final UserRepo userRepo;
    private final RequestRepo requestRepo;
    private final FriendshipRepo friendshipRepo;
    private final RequestServicePublicApi requestService;

    @Override
    public List<FriendshipEventDto> findAll(Long userId) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Set<Long> friendshipIds = friendshipRepo.findAllByInitiatorIdAndStatusTrue(userId).stream()
                .map(it -> it.getFriend().getId())
                .collect(Collectors.toSet());
        final Map<User, List<EventFullDto>> map = new HashMap<>();
        for (Long id : friendshipIds) {
            final User friend = userRepo.findById(id).orElseThrow(
                    () -> new EntityNotFoundException(String.format("User with id=%d was not found", id))
            );
            final List<Long> ids = requestRepo.findAllByRequesterIdAndStatusEquals(id, Status.CONFIRMED).stream()
                    .map(Request::getEvent)
                    .map(Event::getId)
                    .collect(Collectors.toList());
            final List<EventFullDto> events = requestService.findAll(userId, ids);
            if (!map.containsKey(friend)) {
                map.put(friend, events);
            }
        }
        return map.entrySet().stream()
                .map(entry -> FriendshipMapper.toFriendshipEventDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FriendshipDto save(Long userId, Long friendId) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final User friendWrap = userRepo.findById(friendId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", friendId))
        );
        final Friendship friendship = FriendshipMapper.toFriendship(userWrap, friendWrap);
        final Friendship friendshipWrap = friendshipRepo.save(friendship);
        return FriendshipMapper.toFriendshipDto(friendshipWrap);
    }

    @Override
    @Transactional
    public FriendshipDto update(UpdateFriendshipRequest updateFriendshipRequest, Long userId, Long id) {
        final User friendWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Friendship friendshipWrap = friendshipRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Friendship with id=%d was not found", id))
        );
        if (!friendshipWrap.getFriend().getId().equals(userId)) {
            throw new ConflictException(String.format("User with id=%d of the friendship request, cannot accept or reject", userId));
        }
        Optional.ofNullable(updateFriendshipRequest.getStatus()).ifPresent(friendshipWrap::setStatus);
        return FriendshipMapper.toFriendshipDto(friendshipWrap);
    }

    @Override
    @Transactional
    public void deleteById(Long userId, Long id) {
        final User userWrap = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", userId))
        );
        final Friendship friendshipWrap = friendshipRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Friendship with id=%d was not found", id))
        );
        if (friendshipWrap.getInitiator().getId().equals(userId) || friendshipWrap.getFriend().getId().equals(userId)) {
            friendshipRepo.deleteById(friendshipWrap.getId());
        } else {
            throw new ConflictException(String.format("User with id=%d not сan delete from friends", userId));
        }
    }
}