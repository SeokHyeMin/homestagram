package com.home.inmy.controller;

import com.home.inmy.domain.CurrentUser;
import com.home.inmy.service.impl.CommentService;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Comments;
import com.home.inmy.domain.entity.Post;
import com.home.inmy.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor @Slf4j
public class CommentController {

    private final CommentService commentService;
    private final PostServiceImpl postService;

    private static final String COMMENT_PATH = "posts/post-detail :: #comment-list";

    @PostMapping("/comment/save/{post_id}")
    public String commentSave(@PathVariable Long post_id, @RequestParam String comment, @CurrentUser Account account, Model model){

        Post post = postService.getPost(post_id);

        commentService.commentSave(comment, post, account);

        Page<Comments> comments = commentService.getComments(post, 0); //댓글 작성하고 불러올 페이지는 댓글 첫 페이지
        Map<String, Integer> map = getPage(comments); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));
        model.addAttribute("comments", comments);
        model.addAttribute("account",account);

        return COMMENT_PATH;
    }

    @GetMapping("/commentList/{post_id}")
    public String commentListView(@PathVariable Long post_id, @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                  @CurrentUser Account account, Model model){

        Post post = postService.getPost(post_id);
        Page<Comments> comments = commentService.getComments(post, page);
        Map<String, Integer> map = getPage(comments); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));
        model.addAttribute("comments", comments);
        model.addAttribute("account",account);

        return COMMENT_PATH;

    }

    @PostMapping("/deleteComment/{comment_id}/{post_id}")
    public String deleteComment(@PathVariable Long post_id, @PathVariable Long comment_id,
                                @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                @CurrentUser Account account, Model model){

        Post post = postService.getPost(post_id);
        commentService.commentDelete(comment_id); //댓글 삭제

        Page<Comments> comments = commentService.getComments(post, page); //댓글 작성하고 불러올 페이지는 댓글 첫 페이지
        Map<String, Integer> map = getPage(comments); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));
        model.addAttribute("comments", comments);
        model.addAttribute("account",account);

        return COMMENT_PATH;
    }

    private Map<String, Integer> getPage(Page<Comments> commentList){ //페이지 계산하여 시작블럭, 마지막 블럭 담아 반환.

        Map<String, Integer> map = new HashMap<>();

        int pageNum = commentList.getPageable().getPageNumber(); // 현재 페이지
        int pageBlock = 5; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        int totalPage = commentList.getTotalPages();
        endBlockPage = Math.min(totalPage, endBlockPage);

        map.put("startBlockPage",startBlockPage);
        map.put("endBlockPage",endBlockPage);

        return map;
    }
}
