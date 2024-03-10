package com.roclh.mainmodule.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.roclh.common.exceptions.AccountExistsException;
import org.roclh.common.exceptions.AccountNotFountException;
import org.roclh.common.exceptions.ArticleExistsException;
import org.roclh.common.exceptions.ArticleNotFoundException;
import org.roclh.common.exceptions.DataValidationException;
import org.roclh.common.exceptions.ErrorBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ArticleNotFoundException.class})
    protected ResponseEntity<?> handleArticleNotFound(Exception e) {
        ErrorBody errorBody = ErrorBody.builder()
                .message("Article was not found!")
                .details(e.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .stackTrace(e.getStackTrace())
                .timeStamp(LocalDateTime.now())
                .build();
        log.info("Article was not found! {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorBody);
    }

    @ExceptionHandler(value = {ArticleExistsException.class})
    protected ResponseEntity<?> handleArticleExists(Exception e) {
        ErrorBody errorBody = ErrorBody.builder()
                .message("Article already exists!")
                .details(e.getMessage())
                .statusCode(HttpStatus.CONFLICT.value())
                .stackTrace(e.getStackTrace())
                .timeStamp(LocalDateTime.now())
                .build();
        log.info("Article already exists! {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(errorBody);
    }


    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<?> handleIllegalArgument(Exception e) {
        ErrorBody errorBody = ErrorBody.builder()
                .message("Wrong Argument!")
                .details(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .stackTrace(e.getStackTrace())
                .timeStamp(LocalDateTime.now())
                .build();
        log.info("Wrong Argument! {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorBody);
    }


    @ExceptionHandler(value = {DataValidationException.class})
    protected ResponseEntity<?> handleDataValidation(Exception e) {
        ErrorBody errorBody = ErrorBody.builder()
                .message("Data validation error!")
                .details(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .stackTrace(e.getStackTrace())
                .timeStamp(LocalDateTime.now())
                .build();
        log.info("Data validation error! {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorBody);
    }

    @ExceptionHandler(value = {AccountExistsException.class})
    protected ResponseEntity<?> handleAccountExists(Exception e) {
        ErrorBody errorBody = ErrorBody.builder()
                .message("Failed to create an account!") // no additional information about rejection reason
                .details(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .stackTrace(e.getStackTrace())
                .timeStamp(LocalDateTime.now())
                .build();
        log.info("Account already exists! {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorBody);
    }

    @ExceptionHandler(value = {AccountNotFountException.class})
    protected ResponseEntity<?> handleAccountNotFound(Exception e) {
        ErrorBody errorBody = ErrorBody.builder()
                .message("Account not found!")
                .details(e.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .stackTrace(e.getStackTrace())
                .timeStamp(LocalDateTime.now())
                .build();
        log.info("Account not found! {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorBody);
    }
}
