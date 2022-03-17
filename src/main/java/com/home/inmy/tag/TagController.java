package com.home.inmy.tag;

import com.home.inmy.domain.Post;
import com.home.inmy.domain.PostTag;
import com.home.inmy.domain.Tag;
import com.home.inmy.post.PostRepository;
import com.home.inmy.postTag.PostTagRepository;
import com.home.inmy.postTag.PostTagServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class TagController {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    private final PostTagServiceImpl postTagService;
    private final TagService tagService;

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

}
