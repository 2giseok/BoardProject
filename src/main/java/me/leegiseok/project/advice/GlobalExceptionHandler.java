package me.leegiseok.project.advice;

import jakarta.servlet.http.HttpServletRequest;
import me.leegiseok.project.dto.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of("NOT_FOUND",ex.getMessage(),request.getRequestURI()));

        }
        @ExceptionHandler(SecurityException.class)
        public ResponseEntity<ErrorResponse>handleSecurity(SecurityException ex, HttpServletRequest request) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of("FORBIDDEN", ex.getMessage(), request.getRequestURI()));
        }
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public  ResponseEntity<ErrorResponse> handleValidation (MethodArgumentNotValidException ex, HttpServletRequest request) {
            String msg = ex.getBindingResult().getFieldErrors().stream()
                    .map(f -> f.getField()+ ":" + f.getDefaultMessage())
                    .findFirst().orElse("validation error");
            return  ResponseEntity.badRequest().body(ErrorResponse.of("Bad_REQUEST", msg, request.getRequestURI()));
        }
        @ExceptionHandler(Exception.class)
        public  ResponseEntity<ErrorResponse> handleEtc(Exception ex, HttpServletRequest request) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of("INTERNAL_ERROR", ex.getMessage(),request.getRequestURI()));
        }

    }

