package com.home.inmy.post;

import com.home.inmy.account.CurrentUser;
import com.home.inmy.domain.*;
import com.home.inmy.like.LikeService;
import com.home.inmy.postTag.PostTagRepository;
import com.home.inmy.postTag.PostTagServiceImpl;
import com.home.inmy.images.FileStore;
import com.home.inmy.post.form.PostForm;
import com.home.inmy.tag.TagService;
import com.home.inmy.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostServiceImpl postService;
    private final PostTagServiceImpl postTagService;
    private final LikeService likeService;
    private final TagService tagService;

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

        return "posts/new-post";
    }

    @Transactional
    @GetMapping("/post/{post_num}")
    public String postView(@PathVariable Long post_num, Model model, @CurrentUser Account account) {

        Post post = postService.getPost(post_num);

        List<PostTag> postTagList = postTagService.getPostTagList(post);

        postService.updateViews(post); //조회수 증가

        model.addAttribute(post);
        model.addAttribute(postTagList);
        model.addAttribute(account);

        model.addAttribute("isOwner",post.getAccount().getLoginId().equals(account.getLoginId())); //현재 로그인한 계정과 프로필 주인이 같으면 true
        model.addAttribute("like",likeService.accountPostLike(post, account)); //현재 로그인한 계정이 해당 게시글을 좋아요 눌렀다면 true

        return "posts/post-detail";
    }

    @PostMapping("/new-post")
    public String newPostSave(@CurrentUser Account account, @Valid PostForm postForm, String tags, Errors errors, Model model,
                              RedirectAttributes redirectAttributes) throws JSONException, IOException {

        if (errors.hasErrors()) {
            log.info("post save error");
            return "posts/new-post";
        }


        PostDto postDto = postForm.createBoardPostDto(account);
        Post newPost = postService.newPostSave(postDto);

        postTagService.tagSave(newPost, tags);

        model.addAttribute(account);
        redirectAttributes.addAttribute("post_num", newPost.getPost_num());

        return "redirect:/post/{post_num}";
    }

    @Transactional(readOnly = true)
    @GetMapping("/postList")
    public String postList(Model model, @CurrentUser Account account){

        String jpql = "select distinct p from Post p join fetch p.account join fetch p.imageFiles";
        List<Post> postList = em.createQuery(jpql, Post.class).getResultList();

        List<Likes> likes = likeService.getLikeList(account);
        List<Long> postNumList = likeService.getLikePostNum(likes);

        model.addAttribute(postList);
        model.addAttribute("likes", likes);
        model.addAttribute("postNumList", postNumList);
        model.addAttribute(account);

        return "posts/post-list";
    }

    @GetMapping("/post-update/{post_num}")
    public String postUpdateView(@PathVariable Long post_num, @CurrentUser Account account, Model model){

        Post post = postService.getPost(post_num);

        PostForm postForm = PostForm.builder()
                            .title(post.getTitle())
                            .category(post.getCategory())
                            .content(post.getContent())
                            .build();

        List<PostTag> tagList = postTagService.getPostTagList(post);
        List<String> tags = tagService.getTagList(tagList);

        model.addAttribute(account);
        model.addAttribute(post_num);
        model.addAttribute(postForm);
        model.addAttribute("tagStr", String.join("," ,tags));

        return "posts/post-update";
    }


    @PostMapping("/post-update/{post_num}")
    public String postUpdate(@PathVariable Long post_num, @CurrentUser Account account, @Valid PostForm postForm, Errors errors, Model model,
                             RedirectAttributes redirectAttributes) throws IOException{

        if (errors.hasErrors()) {
            log.info("post update error");
            return "posts/new-post";
        }

        model.addAttribute(account);

        PostDto postDto = postForm.createBoardPostDto(account);
        Post post = postService.updatePost(postDto, post_num);

       redirectAttributes.addAttribute("post_num", post.getPost_num());

        return "redirect:/post/{post_num}";
    }

    @GetMapping("/post-delete/{post_num}")
    public String postDelete(@PathVariable Long post_num, @CurrentUser Account account, Model model, RedirectAttributes redirectAttributes){

        postService.deletePost(post_num);

        model.addAttribute(account);

        redirectAttributes.addAttribute("message","해당 글을 삭제하였습니다.");

        return "redirect:/postList";
    }

}