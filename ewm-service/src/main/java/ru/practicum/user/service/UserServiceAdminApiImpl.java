package ru.practicum.user.service;

import java.util.List;
import java.util.stream.Collectors;
import ru.practicum.user.model.User;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.repo.UserRepo;
import ru.practicum.user.mapper.UserMapper;
import org.springframework.data.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceAdminApiImpl implements UserServiceAdminApi {
    private final UserRepo userRepo;

    @Override
    public List<UserDto> findAll(List<Long> ids, Integer from, Integer size) {
        if (!ids.isEmpty()) {
            return userRepo.findAllByIdIn(ids).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return userRepo.findAll(PageRequest.of((from / size), size)).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto save(NewUserRequest userRequest) {
        final User user = UserMapper.toUser(userRequest);
        final User userWrap = userRepo.save(user);
        return UserMapper.toUserDto(userWrap);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        final User userWrap = userRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%d was not found", id))
        );
        userRepo.deleteById(userWrap.getId());
    }
}