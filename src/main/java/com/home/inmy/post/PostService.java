package com.home.inmy.post;

import com.home.inmy.domain.Post;
import com.home.inmy.web.dto.PostDto;

import java.io.IOException;

public interface PostService {

    Post newPostSave(PostDto postDto) throws IOException;
    Post updatePost(PostDto postDto, Long id) throws IOException;
    void updateViews(Post post);
    void deletePost(Long id);
    Post getPost(Long id);
}
