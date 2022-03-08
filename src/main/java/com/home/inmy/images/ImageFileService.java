package com.home.inmy.images;

import com.home.inmy.domain.ImageFile;
import com.home.inmy.domain.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageFileService {

    public abstract void saveImageFile(Post post, List<MultipartFile> multipartFiles) throws IOException;

    public abstract List<ImageFile> findImageFile();

    void deleteImageFile(Post post);
}
