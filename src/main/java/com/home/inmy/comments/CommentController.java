package com.home.inmy.comments;

import com.home.inmy.account.CurrentUser;
import com.home.inmy.domain.Account;
import com.home.inmy.domain.Comments;
import com.home.inmy.domain.Post;
import com.home.inmy.post.PostServiceImpl;
import com.home.inmy.web.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostServiceImpl postService;

    @PostMapping("/comment/save/{post_id}")
    public String commentSave(@PathVariable Long post_id, @RequestParam String comment, @CurrentUser Account account, Model model){

        Post post = postService.getPost(post_id);

        commentService.commentSave(comment, post, account);

        Page<Comments> comments = commentService.getComments(post, 0); //댓글 작성하고 불러올 페이지는 댓글 첫 페이지
        int pageNum = comments.getPageable().getPageNumber(); // 현재 페이지
        int pageBlock = 5; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        int totalPage = comments.getTotalPages();
        endBlockPage = Math.min(totalPage, endBlockPage);

        model.addAttribute("startBlockPage",startBlockPage);
        model.addAttribute("endBlockPage",endBlockPage);
        model.addAttribute("comments", comments);
        model.addAttribute("account",account);

        return "posts/post-detail :: #comment-list";
    }

    @GetMapping("/commentList/{post_id}")
    public String commentListView(@PathVariable Long post_id, @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                  @CurrentUser Account account, Model model){

        Post post = postService.getPost(post_id);
        Page<Comments> comments = commentService.getComments(post, page);
        int pageNum = comments.getPageable().getPageNumber(); // 현재 페이지
        int pageBlock = 5; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        int totalPage = comments.getTotalPages();
        endBlockPage = Math.min(totalPage, endBlockPage);

        model.addAttribute("startBlockPage",startBlockPage);
        model.addAttribute("endBlockPage",endBlockPage);
        model.addAttribute("comments", comments);
        model.addAttribute("account",account);

        return "posts/post-detail :: #comment-list";

    }
}
