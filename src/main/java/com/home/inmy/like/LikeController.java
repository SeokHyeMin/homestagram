package com.home.inmy.like;

import com.home.inmy.account.CurrentUser;
import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import com.home.inmy.post.PostRepository;
import com.home.inmy.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private PostRepository postRepository;
    private LikeRepository likeRepository;

    private PostService postService;
    private LikeService likeService;

    @GetMapping("/like/add")
    @ResponseBody
    public Long addLike(Long post_num, @CurrentUser Account account) {

        Post post = postRepository.findById(post_num).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

        postService.increaseLikes(post); //게시글 좋아요 수 증가

        likeService.addLike(post, account);

        return post.getLikes();
    }

    @GetMapping("/like/remove")
    @ResponseBody
    public Long removeLike(Long post_num, @CurrentUser Account account) {

        Post post = postRepository.findById(post_num).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

        postService.decreaseLikes(post); //게시글 좋아요 수 감소

        likeService.removeLike(post, account);

        return post.getLikes();
    }
}
