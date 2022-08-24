package com.note.yeonglog.service;

import com.note.yeonglog.domain.Post;
import com.note.yeonglog.exception.PostNotFound;
import com.note.yeonglog.repository.PostRepository;
import com.note.yeonglog.request.PostCreate;
import com.note.yeonglog.request.PostEdit;
import com.note.yeonglog.request.PostSearch;
import com.note.yeonglog.response.PostResponse;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
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
    public void clean() {
        postRepository.deleteAll();
    }

    @Test
    public void test() {
        PostCreate postCreate = PostCreate
                .builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postService.write(postCreate);

        assertEquals(1, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    public void test2() {

        Post entity = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(entity);


        //geiven
        PostResponse post = postService.get(entity.getId());

        assertNotNull(post);
        assertEquals(1, postRepository.count());
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

    @Test
    @DisplayName("글 첫페이지 조회")
    void test4() throws Exception {
        //given
        List<Post> requestStream = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo_" + i)
                        .content("bar_" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestStream);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(10, posts.size());
        assertEquals("foo_19", posts.get(0).getTitle());


    }

    @Test
    @DisplayName("글 수정")
    void test5() throws Exception {
        //given
        Post savePost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(savePost);

        PostEdit postEdit = PostEdit.builder()
                .title("foo_edit")
                .build();
        //when
        postService.edit(savePost.getId(), postEdit);

        //then
        Post editPost = postRepository.findById(savePost.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. " + savePost.getId()));

        assertEquals("foo_edit", editPost.getTitle());
        assertEquals("bar", editPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test6() throws Exception {
        //given
        Post savePost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(savePost);

        PostEdit postEdit = PostEdit.builder()
                .content("bar_edit")
                .build();
        //when
        postService.edit(savePost.getId(), postEdit);

        //then
        Post editPost = postRepository.findById(savePost.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. " + savePost.getId()));

        assertEquals("foo", editPost.getTitle());
        assertEquals("bar_edit", editPost.getContent());
    }

    @Test
    @DisplayName("글 삭제")
    public void test7(){
        //given
        Post savePost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(savePost);

        //when
        postService.delete(savePost.getId());
        //then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    public void test8() {

        //given
        Post savePost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(savePost);

        //when
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            PostResponse post = postService.get(savePost.getId() + 1);
        });

        //then
        assertEquals("존재하지 않은 글입니다.", e.getMessage());
    }

    @Test
    @DisplayName("글 삭제 - 존재하지 않는 글")
    public void test9(){
        //given
        Post savePost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(savePost);

        //when
        //when
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.delete(savePost.getId() + 1);
        });

        //then
        assertEquals("존재하지 않은 글입니다.", e.getMessage());

    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않느 글")
    void test10() throws Exception {
        //given
        Post savePost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(savePost);

        PostEdit postEdit = PostEdit.builder()
                .content("bar_edit")
                .build();
        //when
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.edit(savePost.getId() + 1, postEdit);
        });
        //then
        assertEquals("존재하지 않은 글입니다.", e.getMessage());

    }


}