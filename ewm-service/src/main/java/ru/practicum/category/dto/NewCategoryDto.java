package ru.practicum.category.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank
    private String name;
}