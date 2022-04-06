package com.home.inmy.controller;

import com.home.inmy.domain.CurrentUser;
import com.home.inmy.repository.TagRepository;
import com.home.inmy.service.BookmarkService;
import com.home.inmy.service.LikeService;
import com.home.inmy.service.PostTagService;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.PostTag;
import com.home.inmy.domain.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class TagController {

    private final TagRepository tagRepository;

    private final PostTagService postTagService;
    private final LikeService likeService;
    private final BookmarkService bookmarkService;

    //태그 검색
    @GetMapping("/searchTag")       //pageSelect로 검색창을 통해 접근(false)했는지, 글 목록에서 정렬 혹은 페이지 번호를 통해 접근(true)했는지 확인
    public String searchPostByTag(Model model, @CurrentUser Account account, @RequestParam String tagTitle,
                                  @RequestParam(required = false, defaultValue = "false") String pageSelect,
                                  @RequestParam(required = false, defaultValue = "writeTime", value = "orderBy") String orderBy,
                                  @RequestParam(required = false, defaultValue = "0", value = "page") int page){

        Tag tag = tagRepository.findByTagTitle(tagTitle);

        Page<PostTag> postTagList = postTagService.searchPostByTag(tag, page, orderBy);

        List<Long> likeList = new ArrayList<>();
        List<Long> bookmarkList = new ArrayList<>();

        if(account != null){
            likeList = likeService.getLikeList(account) //좋아요한 리스트를 찾아
                    .stream().map(like -> like.getPost().getId()).collect(Collectors.toList()); //해당 글 번호를 리스트에 담아 반환
            bookmarkList = bookmarkService.getBookmarkList(account) //북마크한 리스트를 찾아
                    .stream().map(bookmark -> bookmark.getPost().getId()).collect(Collectors.toList()); // 해당 글 번호를 리스트에 담아 반환
        }

        //페이지 계산
        int pageNum = postTagList.getPageable().getPageNumber(); // 현재 페이지
        int pageBlock = 5; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        int totalPage = postTagList.getTotalPages();
        endBlockPage = Math.min(totalPage, endBlockPage);

        model.addAttribute("startBlockPage", startBlockPage);
        model.addAttribute("endBlockPage", endBlockPage);

        model.addAttribute("postTagList", postTagList);
        model.addAttribute("likeList", likeList);
        model.addAttribute("bookmarkList", bookmarkList);
        model.addAttribute("account",account);
        model.addAttribute("message",tagTitle);

        if(pageSelect.equals("true")){ //유저가 정렬 박스를 눌렀다면 postList-div만 바뀌게
            return "posts/search-post-list :: #postList-div";
        }

        //처음 검색한 경우, 페이지 자체를 새로 보여줌.
        return "posts/search-post-list";
    }

}
