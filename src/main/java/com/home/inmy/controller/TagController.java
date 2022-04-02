package com.home.inmy.controller;

import com.home.inmy.domain.CurrentUser;
import com.home.inmy.repository.TagRepository;
import com.home.inmy.service.impl.BookmarkServiceImpl;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.PostTag;
import com.home.inmy.domain.entity.Tag;
import com.home.inmy.service.impl.LikeServiceImpl;
import com.home.inmy.service.impl.PostTagServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TagController {

    private final TagRepository tagRepository;

    private final PostTagServiceImpl postTagService;
    private final LikeServiceImpl likeService;
    private final BookmarkServiceImpl bookmarkService;


    @GetMapping("/searchTag")
    public String searchPostByTag(@RequestParam String tagTitle, @CurrentUser Account account, Model model){

        Tag tag = tagRepository.findByTagTitle(tagTitle);

        Page<PostTag> postTagList = postTagService.searchPostByTag(tag, 0);

        List<Long> likePostNumList = likeService.getLikePostNum(likeService.getLikeList(account)); //좋아요한 리스트를 찾아 해당 글 번호를 리스트에 담아 반환
        List<Long> bookmarkPostNumList = bookmarkService.getLikePostNum(bookmarkService.getBookmarkList(account)); //북마크한 리스트를 찾아 해당 글 번호를 리스트에 담아 반환

        Map<String, Integer> map = getPage(postTagList); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));

        model.addAttribute("postTagList", postTagList);
        model.addAttribute("postNumList", likePostNumList);
        model.addAttribute("bookmarkPostNum", bookmarkPostNumList);
        model.addAttribute("account",account);
        model.addAttribute("message",tagTitle);

        return "posts/search-post-list";
    }

    @GetMapping("/searchTag/orderBy")
    public String searchTagOrderBy(Model model, @CurrentUser Account account, @RequestParam String tagTitle,
                                  @RequestParam(required = false, defaultValue = "writeTime", value = "orderBy") String orderBy,
                                  @RequestParam(required = false, defaultValue = "0", value = "page") int page){

        Tag tag = tagRepository.findByTagTitle(tagTitle);

        Page<PostTag> postTagList = postTagService.searchPostByTag(tag, page, orderBy);

        List<Long> likePostNumList = likeService.getLikePostNum(likeService.getLikeList(account)); //좋아요한 리스트를 찾아 해당 글 번호를 리스트에 담아 반환
        List<Long> bookmarkPostNumList = bookmarkService.getLikePostNum(bookmarkService.getBookmarkList(account)); //북마크한 리스트를 찾아 해당 글 번호를 리스트에 담아 반환

        Map<String, Integer> map = getPage(postTagList); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));

        model.addAttribute("postTagList", postTagList);
        model.addAttribute("postNumList", likePostNumList);
        model.addAttribute("bookmarkPostNum", bookmarkPostNumList);
        model.addAttribute("account",account);
        model.addAttribute("message",tagTitle);

        return "posts/search-post-list :: #postList-div";
    }

    private Map<String, Integer> getPage(Page<PostTag> postList){ //페이지 계산하여 시작블럭, 마지막 블럭 담아 반환.

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
