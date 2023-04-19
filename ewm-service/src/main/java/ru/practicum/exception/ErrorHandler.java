package ru.practicum.exception;

import java.util.List;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflictException(final ConflictException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(e.getLocalizedMessage(), "For the requested operation the conditions are not met",
                HttpStatus.CONFLICT.name(), LocalDateTime.now(), ofStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(e.getLocalizedMessage(), "Required request body is missing",
                HttpStatus.CONFLICT.name(), LocalDateTime.now(), ofStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(e.getLocalizedMessage(), "Integrity constraint has been violated",
                HttpStatus.CONFLICT.name(), LocalDateTime.now(), ofStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(e.getLocalizedMessage(), "Validation failed",
                HttpStatus.BAD_REQUEST.name(), LocalDateTime.now(), ofStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(e.getLocalizedMessage(), "Required request parameter",
                HttpStatus.BAD_REQUEST.name(), LocalDateTime.now(), ofStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ApiError> handleNumberFormatException(final NumberFormatException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(e.getLocalizedMessage(), "Incorrectly made request",
                HttpStatus.BAD_REQUEST.name(), LocalDateTime.now(), ofStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(final EntityNotFoundException e) {
        log.error(e.getMessage(), e);
        final ApiError error = new ApiError(e.getLocalizedMessage(), "The required object was not found",
                HttpStatus.NOT_FOUND.name(), LocalDateTime.now(), ofStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
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