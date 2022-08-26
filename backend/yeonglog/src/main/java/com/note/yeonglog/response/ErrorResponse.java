package com.note.yeonglog.response;

import com.note.yeonglog.exception.InvalidRequest;
import com.note.yeonglog.exception.YhnohlogException;
import lombok.Builder;
import lombok.Getter;
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

     public ErrorResponse(HttpStatus status, MethodArgumentNotValidException e) {

        this.code = String.valueOf(status.value());
        this.message = "잘못된 요청입니다.";
         for(FieldError fieldError: e.getFieldErrors()){
             validation.put(fieldError.getField(), fieldError.getDefaultMessage());
         }
     }

    public ErrorResponse(YhnohlogException e) {

        this.code = String.valueOf(e.getStatus().value());
        this.message = e.getMessage();
        this.validation.putAll(e.getValidation());
    }

}
