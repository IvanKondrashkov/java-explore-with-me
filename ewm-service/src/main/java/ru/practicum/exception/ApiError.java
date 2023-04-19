package ru.practicum.exception;

import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
    private List<String> errors;
}