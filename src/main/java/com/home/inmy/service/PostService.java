package com.home.inmy.service;

import com.home.inmy.domain.entity.Post;
import com.home.inmy.dto.PostDto;

import java.io.IOException;

public interface PostService {

    Post newPostSave(PostDto postDto) throws IOException;
    Post updatePost(PostDto postDto, Long id) throws IOException;
    void updateViews(Post post);
    void deletePost(Long id);
    Post getPost(Long id);

}
