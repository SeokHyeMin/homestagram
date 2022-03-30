package com.home.inmy.web.dto;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Comments;
import com.home.inmy.domain.Post;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {

    @NotBlank
    private String comment;

    private Post post;

    private Account account;

    @Builder
    public CommentDto(String comment, Post post, Account account){
        this.comment = comment;
        this.post = post;
        this.account = account;
    }

    public Comments createComment(){
        return Comments.builder()
                .comment(comment)
                .account(account)
                .post(post)
                .writeTime(LocalDateTime.now())
                .build();
    }
}
