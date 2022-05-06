package com.home.inmy.controller;

import com.home.inmy.domain.CurrentUser;
import com.home.inmy.domain.entity.*;
import com.home.inmy.service.*;
import com.home.inmy.form.PostForm;
import com.home.inmy.service.impl.TagServiceImpl;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final PostTagService postTagService;
    private final LikeService likeService;
    private final TagService tagService;
    private final BookmarkService bookmarkService;
    private final FollowService followService;
    private final CommentService commentService;

    //글 작성 화면
    @GetMapping("/new-post")
    public String newPostView(Model model, @CurrentUser Account account) {

        model.addAttribute(new PostForm());
        model.addAttribute(account);

        return "posts/new-post";
    }

    @PostMapping("/new-post") // 새 글 작성
    public String newPostSave(@CurrentUser Account account, @Valid PostForm postForm, String tags, Errors errors, Model model,
                              RedirectAttributes redirectAttributes) throws JSONException, IOException {

        log.info("============newPostSave============");

        if (errors.hasErrors()) {
            log.info("post save error");
            return "posts/new-post";
        }

        PostDto postDto = postForm.createBoardPostDto(account);
        Post newPost = postService.newPostSave(postDto);

        //작성한 태그 저장
        postTagService.tagSave(newPost, tags);

        model.addAttribute(account);
        redirectAttributes.addAttribute("id", newPost.getId());

        return "redirect:/post/{id}";
    }

    @Transactional(readOnly = true)
    @GetMapping("/post/{id}") // 글 상세보기
    public String postView(@PathVariable Long id, Model model, @CurrentUser Account account) {

        log.info("============postView============");

        //해당 게시물과, 게시물의 태그 리스트 가져오기
        Post post = postService.getPost(id);
        List<Tag> tagList = postService.getPostTag(post);

        //조회수 증가
        postService.updateViews(post);

        //게시글 주인, 현재 로그인 계정으로 팔로잉 여부 판단.
        Boolean follow = followService.findFollow(post.getAccount().getLoginId(), account);

        //현재 계정이 게시글 주인인지 판단.
        Boolean isOwner = account != null && post.getAccount().getLoginId().equals(account.getLoginId());

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

        model.addAttribute("post",post);
        model.addAttribute("tagList",tagList);
        model.addAttribute("account",account);

        model.addAttribute("follow",follow); //팔로우 여부
        model.addAttribute("isOwner",isOwner); //현재 로그인한 계정과 프로필 주인이 같으면 true
        model.addAttribute("like",likeService.accountPostLike(post, account)); //현재 로그인한 계정이 해당 게시글을 좋아요 눌렀다면 true
        model.addAttribute("bookmark",bookmarkService.accountPostBookmark(post, account)); //현재 로그인한 계정이 해당 게시글을 좋아요 눌렀다면 true

        return "posts/post-detail";
    }

    @Transactional(readOnly = true)
    @GetMapping("/postList") //글 목록 - 커뮤니티
    public String postList(Model model, @CurrentUser Account account,
                           @RequestParam(required = false, defaultValue = "false") String pageSelect,
                           @RequestParam(required = false, defaultValue = "writeTime", value = "orderBy") String orderBy,
                           @RequestParam(required = false, defaultValue = "0", value = "page") int page){

        log.info("============postList============");

        Page<Post> postList = postService.pageList(page, orderBy);

        List<Long> likeList = new ArrayList<>();
        List<Long> bookmarkList = new ArrayList<>();

        //로그인 한 경우에만 좋아요, 북마크한 게시물 리스트 가져오기
        if(account != null){
            likeList = likeService.getLikeList(account) //좋아요한 리스트를 찾아
                    .stream().map(like -> like.getPost().getId()).collect(Collectors.toList()); //해당 글 번호를 리스트에 담아 반환
            bookmarkList = bookmarkService.getBookmarkList(account) //북마크한 리스트를 찾아
                    .stream().map(bookmark -> bookmark.getPost().getId()).collect(Collectors.toList()); // 해당 글 번호를 리스트에 담아 반환
        }

        Map<String, Integer> map = getPage(postList); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));

        model.addAttribute("postList", postList);
        model.addAttribute("likeList", likeList);
        model.addAttribute("bookmarkList", bookmarkList);
        model.addAttribute("account", account);

        if(pageSelect.equals("true")){ //유저가 정렬 박스를 눌렀다면 postList-div만 바뀌게
            return "posts/post-list :: #postList-div";
        }

        //정렬박스를 누른게 아닌 처음 커뮤니티 들어왔을 경우, page를 새로 나타내준다.
        return "posts/post-list";
    }

    @GetMapping("/update-post/{id}") //글 수정화면
    public String postUpdateView(@PathVariable Long id, @CurrentUser Account account, Model model){

        log.info("============postUpdateView============");

        Post post = postService.getPost(id);

        PostForm postForm = PostForm.builder()
                            .title(post.getTitle())
                            .content(post.getContent())
                            .build();

        //해당 게시글의 태그 타이틀 가져오기.
        List<String> tags = postService.getPostTagTitleList(post);
        List<ImageFile> imageFiles = post.getImageFiles();

        model.addAttribute("account",account);
        model.addAttribute("id",id);
        model.addAttribute(postForm);
        model.addAttribute("imageFiles",imageFiles);
        model.addAttribute("tagStr", String.join("," ,tags)); //태그목록을 ,으로 연결하여 한 문장으로 전달 (뷰 나타내기 위해)

        return "posts/post-update";
    }



    @PostMapping("/update-post/{id}") // 글 수정
    public String postUpdate(@PathVariable Long id, @CurrentUser Account account, @Valid PostForm postForm,
                             @RequestParam String tags, @RequestParam String delete_image, Errors errors,
                             Model model, RedirectAttributes redirectAttributes) throws IOException, JSONException {

        log.info("============postUpdate============");

        if (errors.hasErrors()) {
            log.info("post update error");
            return "posts/post-update";
        }

        PostDto postDto = postForm.createBoardPostDto(account);
        Post post = postService.updatePost(postDto, id);
        postTagService.tagSave(post, tags); //변경된 태그를 다시 추가해줌.

        //삭제한 이미지 있는 경우, 해당 이미지 삭제
        if(!delete_image.equals("")){
            postService.deleteImage(post, delete_image);
        }

        model.addAttribute(account);
        redirectAttributes.addAttribute("id", post.getId());

        return "redirect:/post/{id}";
    }

    @PostMapping("/delete-post/{id}") //글 삭제
    public String postDelete(@PathVariable Long id){

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