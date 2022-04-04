package com.home.inmy.service.impl;

import com.home.inmy.domain.entity.ImageFile;
import com.home.inmy.domain.entity.Post;
import com.home.inmy.domain.FileStore;
import com.home.inmy.repository.ImageFileRepository;
import com.home.inmy.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageFileServiceImpl implements ImageFileService {

    private final FileStore fileStore;
    private final ImageFileRepository imageFileRepository;

    @Override
    public void saveImageFile(Post newPost, List<MultipartFile> multipartFiles) throws IOException {

        List<ImageFile> result = fileStore.storeFiles(multipartFiles, newPost);
        newPost.setImageFiles(result);
    }

    @Override
    public void deleteImageFile(Post post, Long id) {
        imageFileRepository.deleteByPostAndId(post, id);
    }

}
