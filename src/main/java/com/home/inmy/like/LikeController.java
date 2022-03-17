package com.home.inmy.like;

import com.home.inmy.account.CurrentUser;
import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import com.home.inmy.post.PostService;
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
