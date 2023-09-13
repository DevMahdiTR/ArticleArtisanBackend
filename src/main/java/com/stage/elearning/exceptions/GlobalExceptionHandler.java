package com.stage.elearning.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExpiredTokenException.class)
    protected ResponseEntity<Object> handleExpiredJwtException(@NotNull ExpiredTokenException ex, WebRequest request){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN)
                .message("Token has expired.")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }

    @ExceptionHandler(InvalidTokenException.class)
    protected ResponseEntity<Object> handleInvalidTokenException(@NotNull InvalidTokenException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message("Invalid token")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }
    @ExceptionHandler(RevokedTokenException.class)
    protected ResponseEntity<Object> handleRevokedTokenException(@NotNull RevokedTokenException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message("Invalid token")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(@NotNull ResourceNotFoundException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message("Resource not found.")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }
    @ExceptionHandler(UnauthorizedActionException.class)
    protected ResponseEntity<Object> handleUnauthorizedActionException(@NotNull UnauthorizedActionException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED)
                .message("The requested action is unauthorized. Access denied.")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(@NotNull IllegalArgumentException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation failed.")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllException(@NotNull Exception exception, WebRequest request){
        List<String> details = new ArrayList<>();
        details.add(exception.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .message("Other exception occurs")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }
    @ExceptionHandler(DisabledException.class)
    protected ResponseEntity<Object> handleDisabledException(@NotNull DisabledException ex){
        List<String> details = new ArrayList<>();
        details.add("The user account is disabled.");

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED)
                .message("User account disabled.")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }
    private HttpStatus determineHttpStatus(int errorCode) {
            Map<Integer, HttpStatus> errorMappings = new HashMap<>();
            errorMappings.put(404, HttpStatus.NOT_FOUND);
            errorMappings.put(409, HttpStatus.CONFLICT);

            return errorMappings.getOrDefault(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NotNull HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .message("Malformed JSON found.")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(@NotNull HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .message("Unsupported Media Type")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message("Method not supported")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error->((FieldError)error).getField() +" : "+ error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation Errors")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@NotNull HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message("Method not supported")
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }
}