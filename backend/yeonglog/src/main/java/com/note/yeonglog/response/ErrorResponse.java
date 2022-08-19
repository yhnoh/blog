package com.note.yeonglog.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    @Builder
     public ErrorResponse(HttpStatus status, MethodArgumentNotValidException exception) {

        this.code = String.valueOf(status.value());
        this.message = "잘못된 요청입니다.";
         for(FieldError fieldError: exception.getFieldErrors()){
             validation.put(fieldError.getField(), fieldError.getDefaultMessage());
         }
     }

}
