package com.home.inmy.images;


import com.home.inmy.domain.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageFileService {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir + filename;
    }

    public List<ImageFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException{

        List<ImageFile> storeFilesResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){
                storeFilesResult.add(storeFile(multipartFile));
            }
        }

        return storeFilesResult;
    }

    private ImageFile storeFile(MultipartFile multipartFile) throws IOException {

        if(multipartFile.isEmpty()){
            return null;
        }

        String origFilename  = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFileName(origFilename);
        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        return new ImageFile(origFilename, storeFilename);
    }

    //UUID 사용해서 서버 내부에서 관리하는 파일명을 생성(유일한 이름으로)
    private String createStoreFileName(String origFilename) {

        String ext = extract(origFilename); //확장자를 별도로 추출하여, 서버 내부에서 관리하는 파일명에 붙여준다.
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extract(String origFilename) {

        int pos = origFilename.lastIndexOf(".");
        return origFilename.substring(pos+1);
    }

}