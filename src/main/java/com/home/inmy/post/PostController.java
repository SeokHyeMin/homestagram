package com.home.inmy.post;

import com.home.inmy.account.CurrentUser;
import com.home.inmy.bookmark.BookmarkServiceImpl;
import com.home.inmy.comments.CommentService;
import com.home.inmy.domain.*;
import com.home.inmy.follow.FollowServiceImpl;
import com.home.inmy.like.LikeServiceImpl;
import com.home.inmy.postTag.PostTagServiceImpl;
import com.home.inmy.post.form.PostForm;
import com.home.inmy.tag.TagRepository;
import com.home.inmy.tag.TagService;
import com.home.inmy.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

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

    private final TagRepository tagRepository;

    @GetMapping("/new-post")
    public String newPostView(Model model, @CurrentUser Account account) {

        model.addAttribute(new PostForm());
        model.addAttribute(account);

        return "posts/new-post";
    }

    @Transactional
    @GetMapping("/post/{id}")
    public String postView(@PathVariable Long id, Model model, @CurrentUser Account account) {

        Post post = postService.getPost(id);

        List<PostTag> postTagList = postTagService.getPostTagList(post);
        Boolean follow = followService.findFollow(post.getAccount().getLoginId(), account); //로그인 계정, 프로필 주인 계정
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

        model.addAttribute(post);
        model.addAttribute(postTagList);
        model.addAttribute("account",account);
        model.addAttribute("comments",comments);
        model.addAttribute("follow",follow); //팔로우 여부
        model.addAttribute("isOwner",post.getAccount().getLoginId().equals(account.getLoginId())); //현재 로그인한 계정과 프로필 주인이 같으면 true
        model.addAttribute("like",likeService.accountPostLike(post, account)); //현재 로그인한 계정이 해당 게시글을 좋아요 눌렀다면 true
        model.addAttribute("bookmark",bookmarkService.accountPostBookmark(post, account)); //현재 로그인한 계정이 해당 게시글을 좋아요 눌렀다면 true

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
        redirectAttributes.addAttribute("id", newPost.getId());

        return "redirect:/post/{id}";
    }

    @Transactional(readOnly = true)
    @GetMapping("/postList")
    public String postList(Model model, @CurrentUser Account account, @RequestParam(required = false, defaultValue = "0", value = "page") int page){

        Page<Post> postList = postService.pageList(page);

        List<Long> likePostNumList = likeService.getLikePostNum(likeService.getLikeList(account)); //좋아요한 리스트를 찾아 해당 글 번호를 리스트에 담아 반환
        List<Long> bookmarkPostNumList = bookmarkService.getLikePostNum(bookmarkService.getBookmarkList(account)); //북마크한 리스트를 찾아 해당 글 번호를 리스트에 담아 반환

        Map<String, Integer> map = getPage(postList); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));

        model.addAttribute("postList", postList);
        model.addAttribute("postNumList", likePostNumList);
        model.addAttribute("bookmarkPostNum", bookmarkPostNumList);
        model.addAttribute(account);

        return "posts/post-list";
    }

    @Transactional(readOnly = true)
    @GetMapping("/postList/orderBy")
    public String postListOrderBy(Model model, @CurrentUser Account account,
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
        model.addAttribute(account);

        return "posts/post-list :: #postList-div";
    }

    @GetMapping("/post-update/{id}")
    public String postUpdateView(@PathVariable Long id, @CurrentUser Account account, Model model){

        Post post = postService.getPost(id);

        PostForm postForm = PostForm.builder()
                            .title(post.getTitle())
                            .content(post.getContent())
                            .build();

        List<PostTag> tagList = postTagService.getPostTagList(post);
        List<String> tags = tagService.getTagList(tagList);

        model.addAttribute(account);
        model.addAttribute(id);
        model.addAttribute(postForm);
        model.addAttribute("tagStr", String.join("," ,tags));

        return "posts/post-update";
    }

    @PostMapping("/post-update/{id}")
    public String postUpdate(@PathVariable Long id, @CurrentUser Account account, @Valid PostForm postForm, Errors errors, Model model,
                             RedirectAttributes redirectAttributes) throws IOException{

        if (errors.hasErrors()) {
            log.info("post update error");
            return "posts/new-post";
        }

        model.addAttribute(account);

        PostDto postDto = postForm.createBoardPostDto(account);
        Post post = postService.updatePost(postDto, id);

       redirectAttributes.addAttribute("id", post.getId());

        return "redirect:/post/{id}";
    }

    @GetMapping("/post-delete/{id}")
    public String postDelete(@PathVariable Long id, @CurrentUser Account account, Model model, RedirectAttributes redirectAttributes){

        postService.deletePost(id);

        model.addAttribute(account);

        redirectAttributes.addAttribute("message","해당 글을 삭제하였습니다.");

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