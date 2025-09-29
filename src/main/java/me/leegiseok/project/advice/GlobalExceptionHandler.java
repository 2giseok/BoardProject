package me.leegiseok.project.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import me.leegiseok.project.dto.ErrorResponse;

import me.leegiseok.project.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
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
            return  ResponseEntity.badRequest().body(ErrorResponse.of("BAD_REQUEST", msg, request.getRequestURI()));
        }
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex,
                                                              HttpServletRequest request) {
            String msg = ex.getConstraintViolations().stream()
                    .findFirst()
                    .map(v -> v.getPropertyPath() + ":" + v.getMessage())
                    .orElse("Constraint violation");
            return  ResponseEntity.badRequest()
                    .body(ErrorResponse.of("BAD_REQUEST", msg, request.getRequestURI()));
        }

        @ExceptionHandler(Exception.class)
        public  ResponseEntity<ErrorResponse> handleEtc(Exception ex, HttpServletRequest request) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of("INTERNAL_ERROR", ex.getMessage(),request.getRequestURI()));
        }

        @ExceptionHandler(UnauthorizedException.class)
    public  ResponseEntity<ErrorResponse> unauthorized(UnauthorizedException ex,
                                                       HttpServletRequest request) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of("UNAUTHORIZED", ex.getMessage(),request.getRequestURI()));

        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public  ResponseEntity<ErrorResponse> typeMismatch(MethodArgumentTypeMismatchException ex,
                                                      HttpServletRequest request) {
            String msg = "Invalid parameter type : " + ex.getName();
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.of("BAD_REQUEST",msg,request.getRequestURI()));

        }

    }

