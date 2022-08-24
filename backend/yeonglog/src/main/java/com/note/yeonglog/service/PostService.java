package com.note.yeonglog.service;

import com.note.yeonglog.domain.Post;
import com.note.yeonglog.domain.PostEditor;
import com.note.yeonglog.exception.PostNotFound;
import com.note.yeonglog.repository.PostRepository;
import com.note.yeonglog.request.PostCreate;
import com.note.yeonglog.request.PostEdit;
import com.note.yeonglog.request.PostSearch;
import com.note.yeonglog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate){
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);
    }

    public PostResponse get(Long postId) {

        Post post = checkNullPost(postId);

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return postResponse;


    }

    private Post checkNullPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFound());
    }

    public List<PostResponse> getList(PostSearch postSearch) {

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit){
        Post post = checkNullPost(id);

        PostEditor postEditor = PostEditor.builder()
                .post(post)
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
        return new PostResponse(post.getId(), post.getTitle(), post.getContent());
    }

    public void delete(long id) {
        Post post = checkNullPost(id);
        postRepository.delete(post);
    }

}
