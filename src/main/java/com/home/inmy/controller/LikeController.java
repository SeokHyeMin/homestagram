package com.home.inmy.controller;

import com.home.inmy.domain.CurrentUser;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Post;
import com.home.inmy.service.PostService;
import com.home.inmy.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private final PostService postService;
    private final LikeService likeService;

    @GetMapping("/like/add")
    @ResponseBody
    public int addLike(Long id, @CurrentUser Account account) {

        Post post = postService.getPost(id);
        likeService.addLike(post, account);

        return post.getLikesList().size();
    }

    @GetMapping("/like/remove")
    @ResponseBody
    public int removeLike(Long id, @CurrentUser Account account) {

        Post post =  postService.getPost(id);
        likeService.removeLike(post, account);

        return post.getLikesList().size();
    }
}
