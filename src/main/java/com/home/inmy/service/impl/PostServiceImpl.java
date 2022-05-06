package com.home.inmy.service.impl;

import com.home.inmy.domain.entity.PostTag;
import com.home.inmy.domain.entity.Tag;
import com.home.inmy.repository.PostRepository;
import com.home.inmy.service.PostService;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Post;
import com.home.inmy.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostTagServiceImpl postTagService;
    private final ImageFileServiceImpl imageFileService;

    private final ModelMapper modelMapper;

    @Override
    public Post newPostSave(PostDto postDto) throws IOException {

        Post newPost = postDto.createPost();
        imageFileService.saveImageFile(newPost, postDto.getImageFiles());

        return postRepository.save(newPost);
    }

    @Override
    public Post updatePost(PostDto postDto, Long id) throws IOException {

        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

        modelMapper.map(postDto, post); //변경감지
        postTagService.deletePost(post); //해당 글 태그들 삭제 (추후에 다시 추가)
        imageFileService.saveImageFile(post, postDto.getImageFiles()); //수정 시 추가한 이미지 파일 추가.

        return post;
    }

    @Override
    public void updateViews(Post post) {
        post.updateViews();
    }

    @Override
    public void deletePost(Long id) { //post와 연관되어 있는 것들 함께 삭제
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

        postRepository.delete(post);
    }

    @Override
    public Post getPost(Long id) {

        return postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 글이 없습니다."));
    }

    @Override
    public Page<Post> mainPost(){
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "views"));
        return postRepository.findAll(pageRequest);
    }

    @Override
    public Page<Post> pageList(int page, String orderBy){

        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, orderBy));
        return postRepository.findAll(pageRequest);
    }

    @Override
    public void deleteImage(Post post, String deleteImage) {

        String[] split = deleteImage.split(",");
        for (String s : split) {
            imageFileService.deleteImageFile(post, Long.valueOf(s));
        }
    }

    @Override
    public Page<Post> profilePageList(Account account, int page){

        PageRequest pageRequest = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "writeTime"));
        return postRepository.findByAccount(account, pageRequest);
    }

    @Override
    public List<String> getPostTagTitleList(Post post) {
        return post.getPostTags().stream().map(postTag -> postTag.getTag().getTagTitle()).collect(Collectors.toList());
    }

    @Override
    public List<Tag> getPostTag(Post post) {
        return post.getPostTags().stream().map(PostTag::getTag).collect(Collectors.toList());
    }
}
