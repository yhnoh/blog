package com.note.yeonglog.repository;

import com.note.yeonglog.domain.Post;
import com.note.yeonglog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
