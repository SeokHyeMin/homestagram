package com.home.inmy.service;

import com.home.inmy.domain.entity.PostTag;
import com.home.inmy.domain.entity.Tag;

import java.util.List;

public interface TagService {


    Tag findOrCreateNew(String tagTitle);

    List<String> getTagList(List<PostTag> tagList);
}
