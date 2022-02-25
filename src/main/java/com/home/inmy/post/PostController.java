package com.home.inmy.post;

import com.home.inmy.web.dto.PostsDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {

    @GetMapping("/post")
    public String postView(Model model){

        model.addAttribute(new PostsDto());

        return "posts/post";
    }
}
