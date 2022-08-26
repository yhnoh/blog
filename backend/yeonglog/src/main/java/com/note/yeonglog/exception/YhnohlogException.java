package com.note.yeonglog.exception;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public abstract class YhnohlogException extends RuntimeException {

    private final Map<String, String> validation = new HashMap();


    public YhnohlogException(String message) {
        super(message);
    }

    public YhnohlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract HttpStatus getStatus();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName, message);
    }

    public Map<String, String> getValidation() {
        return validation;
    }
}
