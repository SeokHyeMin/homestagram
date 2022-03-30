package com.home.inmy.service.impl;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Comments;
import com.home.inmy.domain.entity.Post;
import com.home.inmy.repository.CommentRepository;
import com.home.inmy.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void commentSave(String comment, Post post, Account account){

        CommentDto commentDto = new CommentDto(comment, post, account);
        Comments comments = commentDto.createComment();

        commentRepository.save(comments);
    }

    public Page<Comments> getComments(Post post, int page) {

        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "writeTime"));

        return commentRepository.findAllByPost(post, pageRequest);
    }

    public void commentDelete(Long comment_id) {

        commentRepository.deleteById(comment_id);
    }

    public void commentDeleteByPost(Post post){
        commentRepository.deleteAllByPost(post);
    }

}
