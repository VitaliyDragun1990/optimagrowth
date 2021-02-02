package com.optimagrowth.organization.api.v1.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.optimagrowth.organization.exception.ResourceNotFoundException;
import com.optimagrowth.organization.provider.date.DateTimeProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Set;

import static com.optimagrowth.organization.util.WebUtil.getCurrentRequestPath;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandlerResource extends ResponseEntityExceptionHandler {

    private final DateTimeProvider dateTimeService;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        LOG.warn("Handling ConstraintViolationException:{}", e.getMessage());

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                buildMessageFromConstraintViolation(e.getConstraintViolations()),
                getCurrentRequestPath(request),
                dateTimeService.currentDateTime()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Object> handleConversionIsNotSupportedException(ConversionFailedException e, HttpServletRequest request) {
        LOG.warn("Handling ConversionFailedException:{}", e.getMessage());

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                getCurrentRequestPath(request),
                dateTimeService.currentDateTime()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleConversionIsNotSupportedException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        LOG.warn("Handling MethodArgumentTypeMismatchException:{}", e.getMessage());

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                format("Invalid value for param:[%s]", e.getName()),
                getCurrentRequestPath(request),
                dateTimeService.currentDateTime()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        LOG.warn("Handling ResourceNotFoundException:{}", e.getMessage());

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                getCurrentRequestPath(request),
                dateTimeService.currentDateTime()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e, HttpServletRequest request) {
        LOG.warn("Handling Exception:", e);

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                getCurrentRequestPath(request),
                dateTimeService.currentDateTime()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.warn("Handling MethodArgumentNotValidException:{}", e.getMessage());

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                getCurrentRequestPath(),
                dateTimeService.currentDateTime()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.warn("Handling MissingServletRequestParameterException:{}", e.getMessage());

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                format("Required request parameter:[%s] is missing", e.getParameterName()),
                getCurrentRequestPath(),
                dateTimeService.currentDateTime()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private String buildMessageFromConstraintViolation(Set<ConstraintViolation<?>> violations) {
        return violations.stream().map(ConstraintViolation::getMessage).collect(joining(", "));
    }

    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class ApiError {

        private final LocalDateTime timestamp;

        private final int status;

        private final String error;

        private final String message;

        private final String path;

        public ApiError(HttpStatus status, String message, String path, LocalDateTime timestamp) {
            this.timestamp = timestamp;
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.message = message;
            this.path = path;
        }

        public ApiError(HttpStatus status, String path, LocalDateTime timestamp) {
            this.timestamp = timestamp;
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.message = null;
            this.path = path;
        }
    }
}
