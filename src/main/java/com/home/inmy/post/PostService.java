package com.home.inmy.post;

import com.home.inmy.web.dto.PostsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostsService {

    private final PostRepository postRepository;

    public PostsDto save(PostsDto postDto){
        return postRepository.save(postDto);
    }


}
