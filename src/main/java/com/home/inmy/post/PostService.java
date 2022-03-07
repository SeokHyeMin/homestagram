package com.home.inmy.post;

import com.home.inmy.web.dto.PostDto;

import java.io.IOException;

public interface PostService {

    public abstract Post newPostSave(PostDto postDto) throws IOException;
    public abstract Post updatePost(PostDto postDto , Long post_num) throws IOException;

    void updateViews(Post post);
}
