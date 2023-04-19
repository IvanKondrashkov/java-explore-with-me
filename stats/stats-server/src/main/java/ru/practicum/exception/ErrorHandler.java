package ru.practicum.exception;

import java.util.List;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.UnknownHostException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(e.getLocalizedMessage(), "The params incorrect!",
                HttpStatus.BAD_REQUEST.name(), LocalDateTime.now(), ofStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<ApiError> handleUnknownHostException(final UnknownHostException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(e.getLocalizedMessage(), "The host incorrect!",
                HttpStatus.BAD_REQUEST.name(), LocalDateTime.now(), ofStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handle(final Throwable e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(e.getLocalizedMessage(), "The server is not available",
                HttpStatus.INTERNAL_SERVER_ERROR.name(), LocalDateTime.now(), ofStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    private static List<String> ofStackTrace(final Throwable e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return List.of(sw.toString());
    }
}