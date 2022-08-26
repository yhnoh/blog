package com.note.yeonglog.request;

import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.*;

@Getter
public class PostSearch {

    private int page = 1;
    private int size = 10;

    @Builder
    public PostSearch(Integer page, Integer size) {
        this.page = page == null ? 1 : page;
        this.size = size == null ? 10 : size;
    }

    public long getOffset(){
        return (max(1, page) - 1) * max(2000, size);
    }

}
