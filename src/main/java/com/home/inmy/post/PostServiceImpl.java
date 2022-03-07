package com.home.inmy.post;

import com.home.inmy.images.ImageFile;
import com.home.inmy.images.ImageFileRepository;
import com.home.inmy.images.ImageFileService;
import com.home.inmy.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final ImageFileRepository imageFileRepository;
    private final PostRepository postRepository;

    private final ImageFileService imageFileService;

    private final ModelMapper modelMapper;

    public Post newPostSave(PostDto postDto) throws IOException {

        Post newPost = postDto.createPost();
        imageFileService.saveImageFile(newPost, postDto.getImageFiles());

        return postRepository.save(newPost);
    }

    public Post updatePost(PostDto postDto, Long post_num) throws IOException {

        Post post = postRepository.findById(post_num).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

        modelMapper.map(postDto, post); //변경감지

        imageFileRepository.deleteAll(post.getImageFiles());
        imageFileService.saveImageFile(post, postDto.getImageFiles());

        return post;
    }

    @Override
    public void updateViews(Post post) {
        post.updateViews();
    }

}
