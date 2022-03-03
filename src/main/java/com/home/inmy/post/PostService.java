package com.home.inmy.post;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import com.home.inmy.domain.ImageFile;
import com.home.inmy.web.dto.PostDto;

import java.io.IOException;
import java.util.List;

public interface PostService {

    public abstract Post newPostSave(PostDto postDto) throws IOException;


}
