package com.home.inmy.service;

import com.home.inmy.domain.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageFileService {

    void saveImageFile(Post post, List<MultipartFile> multipartFiles) throws IOException;

    void deleteImageFile(Post post);

    void deleteImageFile(Post post, Long id);
}
