package com.note.yeonglog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.note.yeonglog.domain.Post;
import com.note.yeonglog.repository.PostRepository;
import com.note.yeonglog.request.PostCreate;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    /**
     * Builder 패턴을 사용하는이유
     *
     * 1. 생성자 매개변수 순서를 변경할 수도 있다.
     * 2. 이럴 경우 에러를 발견하기가 힘들다.
     * 2. @Builder는 생성자 위에 붙여주는게 좋다.
     * 4. 왜냐하면 해당 클래스에 필드를 추가하거나 변경하게 될 경우 불리한다.
     * ex) 필드를 추가하는 경우
     * ex) 필드에 final 키워드를 붙이는 경우
     * 빌더의 장점
     * - 가독성이 좋다.
     * - 값 생서에 대한 유연함
     * - 필ㅇ한 값만 받을 수 있다.
     * - 객체의 불변성 필드에 final을 선언하고 값을 계속 변경할 수 있다.
     */
    @Test
    @DisplayName("/posts 요청시 Hello Wrong 를 출력한다.")
    public void test() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();


        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);
        //글제목
        //글내용용

       mockMvc.perform(post("/posts")
                       .content(json)
                       .contentType(APPLICATION_JSON)
               )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }


    @Test
    @DisplayName("/posts 요청시 title 값은 필수다")
    public void test2() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title(null)
                .content("내용입니다.")
                .build();


        String json = objectMapper.writeValueAsString(request);
        //글제목
        //글내용용

        mockMvc.perform(post("/posts")
                        .content(json)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").isNotEmpty())

                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저잗된다.")
    public void test3() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();


        String json = objectMapper.writeValueAsString(request);
        //글제목
        //글내용용

        mockMvc.perform(post("/posts")
                        .content("{\"title\": \"제목입니다.\", \"content\":\"내용입니다.\"}")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        List<Post> requestStream = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("foo_" + i)
                        .content("bar_" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestStream);


        //when
        mockMvc.perform(get("/posts?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Is.is(10)))
                .andExpect(jsonPath("$.[0].title", Is.is("foo_30")))
                .andExpect(jsonPath("$.[0].content", Is.is("bar_30")))
                .andDo(print());


    }


}