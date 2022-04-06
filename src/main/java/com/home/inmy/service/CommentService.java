package com.home.inmy.service;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Comments;
import com.home.inmy.domain.entity.Post;
import org.springframework.data.domain.Page;

public interface CommentService {

    void commentSave(String comment, Post post, Account account);
    Page<Comments> getComments(Post post, int page);
    void commentDelete(Long commentId);
}
