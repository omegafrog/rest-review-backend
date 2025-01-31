package org.example.sbb.app.global.security;

import jakarta.persistence.PersistenceException;
import org.example.sbb.app.global.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvisor {
    @ExceptionHandler(PersistenceException.class)
    public ApiResponse persistenceException(PersistenceException e){
        return new ApiResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), null);
    }
}
