package com.note.yeonglog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class InvalidRequest extends YhnohlogException {
    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fileName, String message){
        super(MESSAGE);
        addValidation(fileName, message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}