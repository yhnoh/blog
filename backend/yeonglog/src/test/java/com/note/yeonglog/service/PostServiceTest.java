package com.note.yeonglog.service;

import com.note.yeonglog.domain.Post;
import com.note.yeonglog.repository.PostRepository;
import com.note.yeonglog.request.PostCreate;
import com.note.yeonglog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void clean(){
        postRepository.deleteAll();
    }

    @Test
    public void test(){
        PostCreate postCreate = PostCreate
                .builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postService.write(postCreate);

        assertEquals(1,  postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    public void test2(){

        Post entity = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(entity);


        //geiven
        PostResponse post = postService.get(entity.getId());

        assertNotNull(post);
        assertEquals(1,  postRepository.count());
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());

    }

    @Test
    @DisplayName("글 한개 조회")
    void test3() throws Exception {
        //given
        Post savePost = Post.builder()
                .title("123456789")
                .content("bar")
                .build();
        postRepository.save(savePost);

        //when
        mockMvc.perform(get("/posts/{postId}", savePost.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savePost.getId()))
                .andExpect(jsonPath("$.title").value("123456789"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

        //then
    }
}