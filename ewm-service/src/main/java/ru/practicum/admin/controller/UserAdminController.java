package ru.practicum.admin.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.service.UserServiceAdminApi;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserServiceAdminApi userService;

    @GetMapping("/users")
    public List<UserDto> findAll(@RequestParam(name = "ids", required = false) List<Long> ids,
                                 @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                 @Positive @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Send get request /admin/users?ids={}&from={}&size={}", ids, from, size);
        return userService.findAll(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Valid @RequestBody NewUserRequest userRequest) {
        log.info("Send post request /admin/users");
        return userService.save(userRequest);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Positive @PathVariable Long id) {
        log.info("Send delete request /admin/users/{}", id);
        userService.deleteById(id);
    }
}