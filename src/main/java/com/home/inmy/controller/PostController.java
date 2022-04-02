package com.home.inmy.controller;

import com.home.inmy.domain.CurrentUser;
import com.home.inmy.domain.entity.*;
import com.home.inmy.service.ImageFileService;
import com.home.inmy.service.impl.PostServiceImpl;
import com.home.inmy.service.impl.BookmarkServiceImpl;
import com.home.inmy.service.impl.CommentService;
import com.home.inmy.service.impl.FollowServiceImpl;
import com.home.inmy.service.impl.LikeServiceImpl;
import com.home.inmy.service.impl.PostTagServiceImpl;
import com.home.inmy.form.PostForm;
import com.home.inmy.service.impl.TagService;
import com.home.inmy.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostServiceImpl postService;
    private final PostTagServiceImpl postTagService;
    private final LikeServiceImpl likeService;
    private final TagService tagService;
    private final BookmarkServiceImpl bookmarkService;
    private final FollowServiceImpl followService;
    private final CommentService commentService;
    private final ImageFileService imageFileService;

    @GetMapping("/new-post")
    public String newPostView(Model model, @CurrentUser Account account) {

        model.addAttribute(new PostForm());
        model.addAttribute(account);

        return "posts/new-post";
    }

    @Transactional
    @GetMapping("/post/{id}") // 글 상세보기
    public String postView(@PathVariable Long id, Model model, @CurrentUser Account account) {

        log.info("--------------postView-------------");
        Post post = postService.getPost(id);

        List<PostTag> postTagList = postTagService.getPostTagList(post);
        Boolean follow = followService.findFollow(post.getAccount().getLoginId(), account); //게시글 주인, 현재 로그인 계정으로 팔로잉 여부 판단.

        Boolean isOwner = account != null && post.getAccount().getLoginId().equals(account.getLoginId()); //현재 계정이 게시글 주인인지 판단.

        //댓글 페이징
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
        postService.updateViews(post); //조회수 증가

        model.addAttribute("post",post);
        model.addAttribute("postTagList",postTagList);
        model.addAttribute("account",account);
        model.addAttribute("comments",comments);
        model.addAttribute("follow",follow); //팔로우 여부
        model.addAttribute("isOwner",isOwner); //현재 로그인한 계정과 프로필 주인이 같으면 true
        model.addAttribute("like",likeService.accountPostLike(post, account)); //현재 로그인한 계정이 해당 게시글을 좋아요 눌렀다면 true
        model.addAttribute("bookmark",bookmarkService.accountPostBookmark(post, account)); //현재 로그인한 계정이 해당 게시글을 좋아요 눌렀다면 true

        return "posts/post-detail";
    }

    @PostMapping("/new-post") // 새 글 작성
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
        redirectAttributes.addAttribute("id", newPost.getId());

        return "redirect:/post/{id}";
    }

    @Transactional(readOnly = true)
    @GetMapping("/postList") //글 목록 - 커뮤니티
    public String postList(Model model, @CurrentUser Account account,
                           @RequestParam(required = false, defaultValue = "false") String pageSelect,
                           @RequestParam(required = false, defaultValue = "writeTime", value = "orderBy") String orderBy,
                           @RequestParam(required = false, defaultValue = "0", value = "page") int page){

        Page<Post> postList = postService.pageList(page, orderBy);

        List<Long> likePostNumList = likeService.getLikePostNum(likeService.getLikeList(account)); //좋아요한 리스트를 찾아 해당 글 번호를 리스트에 담아 반환
        List<Long> bookmarkPostNumList = bookmarkService.getLikePostNum(bookmarkService.getBookmarkList(account)); //북마크한 리스트를 찾아 해당 글 번호를 리스트에 담아 반환

        Map<String, Integer> map = getPage(postList); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));

        model.addAttribute("postList", postList);
        model.addAttribute("postNumList", likePostNumList);
        model.addAttribute("bookmarkPostNum", bookmarkPostNumList);
        model.addAttribute("account", account);

        if(pageSelect.equals("true")){
            return "posts/post-list :: #postList-div";
        }

        return "posts/post-list";
    }

    @GetMapping("/post-update/{id}") //글 수정화면
    public String postUpdateView(@PathVariable Long id, @CurrentUser Account account, Model model){

        log.info("-----------post-update-view------------");
        Post post = postService.getPost(id);

        PostForm postForm = PostForm.builder()
                            .title(post.getTitle())
                            .content(post.getContent())
                            .build();

        List<PostTag> tagList = postTagService.getPostTagList(post);
        List<String> tags = tagService.getTagList(tagList);
        List<ImageFile> imageFiles = post.getImageFiles();

        model.addAttribute("account",account);
        model.addAttribute("id",id);
        model.addAttribute(postForm);
        model.addAttribute("imageFiles",imageFiles);
        model.addAttribute("tagStr", String.join("," ,tags));

        return "posts/post-update";
    }

    @PostMapping("/post-update/{id}") // 글 수정
    public String postUpdate(@PathVariable Long id, @CurrentUser Account account, @Valid PostForm postForm,
                             @RequestParam String tags, @RequestParam String delete_image,
                             Errors errors, Model model, RedirectAttributes redirectAttributes) throws IOException, JSONException {

        if (errors.hasErrors()) {
            log.info("post update error");
            return "posts/post-update";
        }

        log.info("--------post-update----------");
        PostDto postDto = postForm.createBoardPostDto(account);
        Post post = postService.updatePost(postDto, id);
        postTagService.tagSave(post, tags); //변경된 태그를 다시 추가해줌.
        if(!delete_image.equals("")){ //삭제한 이미지 있는 경우, 해당 이미지 삭제
            postService.deleteImage(post, delete_image);
        }

        model.addAttribute(account);
       redirectAttributes.addAttribute("id", post.getId());

        return "redirect:/post/{id}";
    }

    @PostMapping("/post/delete/{id}") //글 삭제
    public String postDelete(@PathVariable Long id){
        log.info("=====postDelete=====");
        postService.deletePost(id); //게시글 삭제

        return "redirect:/postList";
    }

    private Map<String, Integer> getPage(Page<Post> postList){ //페이지 계산하여 시작블럭, 마지막 블럭 담아 반환.

        Map<String, Integer> map = new HashMap<>();

        int pageNum = postList.getPageable().getPageNumber(); // 현재 페이지
        int pageBlock = 5; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        int totalPage = postList.getTotalPages();
        endBlockPage = Math.min(totalPage, endBlockPage);

        map.put("startBlockPage",startBlockPage);
        map.put("endBlockPage",endBlockPage);

        return map;
    }

}