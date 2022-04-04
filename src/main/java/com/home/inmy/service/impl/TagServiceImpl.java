package com.home.inmy.service.impl;

import com.home.inmy.domain.entity.PostTag;
import com.home.inmy.domain.entity.Tag;
import com.home.inmy.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag findOrCreateNew(String tagTitle) {
        Tag tag = tagRepository.findByTagTitle(tagTitle);
        if (tag == null) {
            tag = tagRepository.save(Tag.builder().tagTitle(tagTitle).build());
        }
        return tag;
    }

    public List<String> getTagList(List<PostTag> tagList) {

        List<String> tags = new ArrayList<>();

        for (PostTag postTag : tagList) {
            tags.add(postTag.getTag().getTagTitle());
        }

        return tags;
    }

}
