package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

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