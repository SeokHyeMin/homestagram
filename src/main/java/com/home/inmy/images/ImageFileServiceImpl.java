package com.home.inmy.images;

import com.home.inmy.domain.ImageFile;
import com.home.inmy.domain.Post;
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

    public List<ImageFile> saveImageFile(Post newPost, List<MultipartFile> multipartFiles) throws IOException {

        ImageFile imageFile = new ImageFile();
        List<ImageFile> result = fileStore.storeFiles(multipartFiles, newPost);
        newPost.setImageFiles(result);
        return result;
    }

    public List<ImageFile> findImageFile(){
        return imageFileRepository.findAll();
    }

}
