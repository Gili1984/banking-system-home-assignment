package com.banking.transaction_service.exceptions;

import com.banking.transaction_service.exception.FromAccountServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FromAccountServiceException.class)
    public ResponseEntity<Map<String, Object>> handleFromAccountServiceException(FromAccountServiceException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("statusCode", ex.getStatusCode());
        errorDetails.put("accountServiceError", ex.getErrorBody());

        return ResponseEntity.status(ex.getStatusCode()).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("errorCode", "VALIDATION_ERROR");
        responseBody.put("message", "Validation failed");
        responseBody.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}