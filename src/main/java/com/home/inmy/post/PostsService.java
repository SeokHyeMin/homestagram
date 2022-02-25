package com.home.inmy.post;

import com.home.inmy.web.dto.PostsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postRepository;

    public Long save(PostsDto postDto){
        return postRepository.save(postDto.toEntity()).getPost_num();
    }


}
