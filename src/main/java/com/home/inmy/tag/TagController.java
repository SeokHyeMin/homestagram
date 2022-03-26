package com.home.inmy.tag;

import com.home.inmy.account.CurrentUser;
import com.home.inmy.bookmark.BookmarkServiceImpl;
import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import com.home.inmy.domain.PostTag;
import com.home.inmy.domain.Tag;
import com.home.inmy.like.LikeServiceImpl;
import com.home.inmy.post.PostRepository;
import com.home.inmy.postTag.PostTagRepository;
import com.home.inmy.postTag.PostTagServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TagController {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    private final PostTagServiceImpl postTagService;
    private final TagService tagService;
    private final LikeServiceImpl likeService;
    private final BookmarkServiceImpl bookmarkService;

    @PostMapping("/post-update/{id}/tags/add")
    @ResponseBody
    public ResponseEntity addTag(@PathVariable Long id, @RequestBody TagForm tagForm) {

        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

        Tag tag = tagService.findOrCreateNew(tagForm.getTagTitle());
        postTagService.postTagSave(post, tag);

        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping("/post-update/{id}/tags/remove")
    @ResponseBody
    public ResponseEntity removeTag(@PathVariable Long id, @RequestBody TagForm tagForm) {

        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        Tag tag = tagRepository.findByTagTitle(tagForm.getTagTitle());

        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        PostTag postTag = postTagRepository.findByPostAndTag(post, tag);
        postTagService.deleteTag(postTag);

        return ResponseEntity.ok().build();
    }

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

        return "posts/search-post-list";
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
