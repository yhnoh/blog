package com.note.yeonglog.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {
    private String title;
    private String content;

    @Builder
    public PostEditor(Post post, String title, String content) {

        this.title = title != null ? title : post.getTitle();
        this.content = content != null ? content : post.getContent();
    }
}
