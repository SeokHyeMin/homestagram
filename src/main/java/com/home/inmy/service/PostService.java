package com.home.inmy.service;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Post;
import com.home.inmy.dto.PostDto;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Post newPostSave(PostDto postDto) throws IOException;
    Post updatePost(PostDto postDto, Long id) throws IOException;
    void updateViews(Post post);
    void deletePost(Long id);
    Post getPost(Long id);

    Page<Post> top4PostOrderByViews();

    Page<Post> pageList(int page, String orderBy);

    void deleteImage(Post post, String deleteImage);

    Page<Post> profilePageList(Account accountByLoginId, int page);
}
