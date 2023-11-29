package com.example.reservation.exception;


import static com.example.reservation.domain.constants.ErrorCode.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        return new ResponseEntity<>(
            new ErrorResponse(e.getErrorCode().getStatus(), e.getErrorCode().getDescription()),
            HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorResponse> handleServerException(Exception ex) {
        return new ResponseEntity<>(
            new ErrorResponse(INTERNAL_SERVER_ERROR.getStatus(), INTERNAL_SERVER_ERROR.getDescription()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
