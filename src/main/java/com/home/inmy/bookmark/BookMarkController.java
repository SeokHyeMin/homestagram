package com.home.inmy.bookmark;

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
public class BookMarkController {

    private final PostService postService;
    private final BookmarkServiceImpl bookmarkService;

    @GetMapping("/bookmark/add")
    @ResponseBody
    public int addBookmark(Long post_num, @CurrentUser Account account) {

        Post post = postService.getPost(post_num);
        bookmarkService.addBookmark(post, account);

        return post.getBookmarkList().size();
    }

    @GetMapping("/bookmark/remove")
    @ResponseBody
    public int removeBookmark(Long post_num, @CurrentUser Account account) {

        Post post =  postService.getPost(post_num);
        bookmarkService.removeBookmark(post, account);

        return post.getBookmarkList().size();
    }
}
