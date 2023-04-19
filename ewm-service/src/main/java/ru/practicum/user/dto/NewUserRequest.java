package ru.practicum.user.dto;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank
    private String name;
    @Email
    private String email;
}