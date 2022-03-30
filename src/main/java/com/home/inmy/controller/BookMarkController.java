package com.home.inmy.controller;

import com.home.inmy.domain.CurrentUser;
import com.home.inmy.service.impl.BookmarkServiceImpl;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Post;
import com.home.inmy.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class BookMarkController {

    private final PostService postService;
    private final BookmarkServiceImpl bookmarkService;

    @GetMapping("/bookmark/add")
    @ResponseBody
    public int addBookmark(Long id, @CurrentUser Account account) {

        Post post = postService.getPost(id);
        bookmarkService.addBookmark(post, account);

        return post.getBookmarkList().size();
    }

    @GetMapping("/bookmark/remove")
    @ResponseBody
    public int removeBookmark(Long id, @CurrentUser Account account) {

        Post post =  postService.getPost(id);
        bookmarkService.removeBookmark(post, account);

        return post.getBookmarkList().size();
    }
}
