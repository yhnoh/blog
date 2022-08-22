package com.note.yeonglog.controller;

import com.note.yeonglog.domain.Post;
import com.note.yeonglog.request.PostCreate;
import com.note.yeonglog.response.PostResponse;
import com.note.yeonglog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 데이터를 검증하는 역할
 * 1. client 개발자가 실수로 값을 안보낼 수 있다.
 * 2. client bug로 값이 누락될 수 있다.
 * 3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
 * 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
 * 5. 서버 개발자의 편암함을 위해서
 */

/**
 * 데이터 유효성 검사
 * @Valid 를 활용하여 데이터 유효성 검사
 * BindingResult를 컨트롤러 파라미터로 받으면 해당 에러를 받을 수 있다.
 *
 * jsonPath를 활용하여 json 값을 확인할 수 있다.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {
        //저장한 데이터 엔티티 response로 응답하기
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId){
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@PageableDefault(size = 5) Pageable pageable){
        return postService.getList(pageable);
    }
}
