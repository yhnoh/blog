package com.note.yeonglog.exception;

import org.springframework.http.HttpStatus;

public class PostNotFound extends YhnohlogException {
    private static final String MESSAGE = "존재하지 않은 글입니다.";


    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
