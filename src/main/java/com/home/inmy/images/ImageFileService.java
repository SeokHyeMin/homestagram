package com.home.inmy.images;

import com.home.inmy.post.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageFileService {

    public abstract List<ImageFile> saveImageFile(Post post, List<MultipartFile> multipartFiles) throws IOException;

    public abstract List<ImageFile> findImageFile();
}
