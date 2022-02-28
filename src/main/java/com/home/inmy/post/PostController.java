package com.home.inmy.post;

import com.home.inmy.account.AccountRepository;
import com.home.inmy.account.CurrentUser;
import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import com.home.inmy.images.ImageFile;
import com.home.inmy.images.ImageFileService;
import com.home.inmy.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final ModelMapper modelMapper;
    private final Post_TagService postTagService;
    private final ImageFileService imageFileService;
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    @ModelAttribute("categories")
    public List<Category> categories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("원룸"));
        categories.add(new Category("아파트"));
        categories.add(new Category("주택"));
        categories.add(new Category("빌라"));

        return categories;
    }

    @GetMapping("/new-post")
    public String newPostView(Model model, @CurrentUser Account account) {

        model.addAttribute(new PostDto());
        model.addAttribute(account);

        return "posts/new-post";
    }

    @Transactional(readOnly = true)
    @GetMapping("/post/{post_num}")
    public String postView(@PathVariable Long post_num, Model model, @CurrentUser Account account) {

        Post post = postRepository.findById(post_num).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        String writerLoginId = post.getAuthor();
        Account writer = accountRepository.findByLoginId(writerLoginId);
        model.addAttribute(post);
        model.addAttribute(writer);
        model.addAttribute("nickname",writer.getNickname());
        model.addAttribute("profile",writer.getProfile());
        model.addAttribute("loginId",writer.getLoginId());

        return "posts/post-detail";
    }

    @PostMapping("/new-post")
    public String newPostSave(@CurrentUser Account account, @Valid PostDto postDto, Errors errors, Model model,
                              RedirectAttributes redirectAttributes) throws IOException {

        if (errors.hasErrors()) {
            log.info("post save error");
            return "posts/new-post";
        }

        List<ImageFile> imageFiles = imageFileService.storeFiles(postDto.getImageFiles());

        Post newPost = postService.newPostSave(modelMapper.map(postDto, Post.class), account, imageFiles);

        redirectAttributes.addAttribute("post_num", newPost.getPost_num());


        return "redirect:/post/{post_num}";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + imageFileService.getFullPath(filename));
    }

}