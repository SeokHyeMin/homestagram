package com.home.inmy.tag;

import com.home.inmy.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
