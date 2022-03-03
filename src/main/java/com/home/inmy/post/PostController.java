package com.home.inmy.post;

import com.home.inmy.account.AccountRepository;
import com.home.inmy.account.CurrentUser;
import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import com.home.inmy.domain.ImageFile;
import com.home.inmy.domain.Tag;
import com.home.inmy.images.FileStore;
import com.home.inmy.images.ImageFileRepository;
import com.home.inmy.post.form.PostForm;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostServiceImpl postService;
    private final ModelMapper modelMapper;
    private final FileStore fileStore;
    private final ImageFileRepository imageFileRepository;
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final EntityManager em;

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

        model.addAttribute(new PostForm());
        model.addAttribute(account);
        log.info("new-post-view");
        log.info(account.getLoginId());

        return "posts/new-post";
    }

    @Transactional(readOnly = true)
    @GetMapping("/post/{post_num}")
    public String postView(@PathVariable Long post_num, Model model, @CurrentUser Account account) {

        Post post = postRepository.findById(post_num).orElseThrow(()->new IllegalArgumentException("해당 글이 없습니다."));

        List<ImageFile> imageFiles = imageFileRepository.findByPost(post);

        model.addAttribute(post);
        model.addAttribute(account);
        model.addAttribute("isOwner",post.getAccount().getLoginId().equals(account.getLoginId())); //현재 로그인한 계정과 프로필 주인이 같으면 true


        return "posts/post-detail";
    }

    @PostMapping("/new-post")
    public String newPostSave(@CurrentUser Account account, @Valid PostForm postForm, Errors errors, Model model,
                              RedirectAttributes redirectAttributes) throws IOException {

        if (errors.hasErrors()) {
            log.info("post save error");
            return "posts/new-post";
        }

        model.addAttribute(account);

        PostDto postDto = postForm.createBoardPostDto(account);

        Post newPost = postService.newPostSave(postDto);

        redirectAttributes.addAttribute("post_num", newPost.getPost_num());

        return "redirect:/post/{post_num}";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @Transactional(readOnly = true)
    @GetMapping("/postList")
    public String postList(Model model, @CurrentUser Account account){

        String jpql = "select distinct p from Post p join fetch p.account join fetch p.imageFiles";
        List<Post> postList = em.createQuery(jpql, Post.class).getResultList();

        model.addAttribute(postList);
        model.addAttribute(account);

        return "posts/post-list";
    }

    @GetMapping("/post-update/{post_num}")
    public String postUpdateView(@PathVariable Long post_num, @CurrentUser Account account, Model model){

        Post post = postRepository.findById(post_num).orElseThrow(() -> new IllegalArgumentException("해당하는 글이 없습니다."));

        PostForm postForm = PostForm.builder()
                            .title(post.getTitle())
                            .category(post.getCategory())
                            .content(post.getContent())
                            .build();

        model.addAttribute(account);
        model.addAttribute(postForm);
        model.addAttribute(post_num);

        return "posts/post-update";
    }


    @PostMapping("/post-update/{post_num}")
    public String postUpdate(@PathVariable Long post_num, @CurrentUser Account account, @Valid PostForm postForm, Errors errors, Model model,
                             RedirectAttributes redirectAttributes) throws IOException {

        if (errors.hasErrors()) {
            log.info("post update error");
            return "posts/new-post";
        }

        model.addAttribute(account);

        PostDto postDto = postForm.createBoardPostDto(account);
        log.info("start");
        Post newPost = postService.updatePost(postDto, post_num);

        redirectAttributes.addAttribute("post_num", newPost.getPost_num());
        log.info("end");
        log.info(newPost.getTitle());
        return "redirect:/post/{post_num}";
    }
}