package com.opencommerce.cartservice.exception;

import com.opencommerce.cartservice.dto.response.ErrorResponse;
import com.opencommerce.cartservice.dto.response.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartItemNotFound(CartItemNotFoundException ex,
                                                                HttpServletRequest request){
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Cart Item NotFound",
                ex.getMessage(),
                request.getRequestURI() );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFound(CartNotFoundException ex,
                                                                HttpServletRequest request){
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Cart Not Found",
                ex.getMessage(),
                request.getRequestURI() );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex,
                                                            HttpServletRequest request){
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                ex.getMessage(),
                request.getRequestURI() );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse>
    handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {

                    String field =
                            ((FieldError) error)
                                    .getField();

                    String message =
                            error.getDefaultMessage();

                    errors.put(
                            field,
                            message
                    );
                });

        ValidationErrorResponse response =
                new ValidationErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation Error",
                        errors,
                        request.getRequestURI()
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request ) {

        ErrorResponse response =
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        ex.getMessage(),
                        request.getRequestURI()
                );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
