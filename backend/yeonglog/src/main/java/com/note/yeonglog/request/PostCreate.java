package com.note.yeonglog.request;

import com.note.yeonglog.exception.InvalidRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해 주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해 주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate(){
        if(getTitle().contains("바보")){
            throw new InvalidRequest("title", "바보는 포함할 수 없는 단어입니다.");
        }
    }
}
