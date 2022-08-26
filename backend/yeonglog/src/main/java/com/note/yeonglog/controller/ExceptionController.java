package com.note.yeonglog.controller;

import com.note.yeonglog.exception.PostNotFound;
import com.note.yeonglog.exception.YhnohlogException;
import com.note.yeonglog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e){
        //MethodArgumentNotValidException
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(YhnohlogException.class)
    public ResponseEntity<ErrorResponse> postNotFound(YhnohlogException e){

        ErrorResponse errorResponse = new ErrorResponse(e);

        return ResponseEntity.status(e.getStatus())
                .body(errorResponse);

    }

}
