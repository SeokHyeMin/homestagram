package com.home.inmy.main;

import com.home.inmy.account.CurrentUser;
import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import com.home.inmy.post.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final PostServiceImpl postService;

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model){
        if(account != null){
            model.addAttribute(account);
        }

        List<Post> postList = postService.top4PostOrderByViews(); //조회수 높은 글 4개

        model.addAttribute("postList",postList);

        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false, value = "error") String error,
                        @RequestParam(required = false, value = "exception") String exception, Model model){

        model.addAttribute("error", error);
        model.addAttribute("exception",exception);

        return "login";
    }

}
