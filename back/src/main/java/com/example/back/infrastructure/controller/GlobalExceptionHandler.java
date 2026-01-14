package com.example.back.infrastructure.controller;

import com.example.back.domain.exception.EmailAlreadyInUseException;
import com.example.back.domain.exception.UserNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(NOT_FOUND).body(
                new ErrorResponse(
                        NOT_FOUND.value(),
                        ex.getMessage(),
                        Instant.now().toString()));
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleEmailInUse(EmailAlreadyInUseException ex) {
        return ResponseEntity.status(CONFLICT).body(
                new ErrorResponse(CONFLICT.value(), ex.getMessage(), Instant.now().toString()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(CONFLICT).body(
                new ErrorResponse(CONFLICT.value(), "Email already in use", Instant.now().toString()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(
                new ErrorResponse(BAD_REQUEST.value(), ex.getMessage(), Instant.now().toString()));
    }

    public record ErrorResponse(int status, String message, String timestamp) {
    }
}
