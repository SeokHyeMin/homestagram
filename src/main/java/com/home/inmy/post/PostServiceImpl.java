package com.home.inmy.post;

import com.home.inmy.images.ImageFile;
import com.home.inmy.images.ImageFileRepository;
import com.home.inmy.images.ImageFileService;
import com.home.inmy.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

@Service @Slf4j
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final ImageFileRepository imageFileRepository;
    private final PostRepository postRepository;
    private final EntityManager em;
    private final ImageFileService imageFileService;
    private final ModelMapper modelMapper;

    public Post newPostSave(PostDto postDto) throws IOException {

        Post newPost = postDto.createPost();
        List<ImageFile> imageFiles = imageFileService.saveImageFile(newPost, postDto.getImageFiles());

        return postRepository.save(newPost);
    }

    public Post updatePost(PostDto postDto, Long post_num) throws IOException {

        log.info("update start");
        Post newPost = postRepository.findById(post_num).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

        modelMapper.map(postDto, newPost);

        imageFileRepository.deleteAll(newPost.getImageFiles());
        List<ImageFile> imageFiles = imageFileService.saveImageFile(newPost, postDto.getImageFiles());

        log.info("update Post ok");
        return newPost;
    }


}
