package com.fx.spring_boot_application.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(Exception e) {
        log.error("Handle exception {}", e);
        return ResponseEntity
            .status(400)
            .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        log.error("Handle exception {}", e);
        return ResponseEntity
            .status(500)
            .body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Handle entity not found exception {}", e);
        return ResponseEntity
            .status(404)
            .body(e.getMessage());
    }

    @ExceptionHandler(exception = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> handleIllegalException(Exception e) {
        log.error("Handle illegal exception {}", e);
        return ResponseEntity
            .status(404)
            .body(e.getMessage());
    }
}
