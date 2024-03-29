package com.home.inmy.domain;


import com.home.inmy.domain.entity.ImageFile;
import com.home.inmy.domain.entity.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}") //실제 파일이 저장될 경로
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir + filename;
    }


    public List<ImageFile> storeFiles(List<MultipartFile> multipartFiles, Post post) throws IOException{

        List<ImageFile> storeFilesResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){
                storeFilesResult.add(storeFile(multipartFile, post));
            }
        }

        return storeFilesResult;
    }

    private ImageFile storeFile(MultipartFile multipartFile, Post post) throws IOException {

        if(multipartFile.isEmpty()){
            return null;
        }

        String origFilename  = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFileName(origFilename);
        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        return new ImageFile(origFilename, storeFilename, post);
    }

    //UUID 사용해서 서버 내부에서 관리하는 파일명을 생성(유일한 이름으로)
    private String createStoreFileName(String origFilename) {

        String ext = extract(origFilename); //확장자를 별도로 추출하여, 서버 내부에서 관리하는 파일명에 붙여준다.
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext; //파일명 생성 후, 파일확장자와 함께 반환
    }

    private String extract(String origFilename) {

        int pos = origFilename.lastIndexOf(".");
        return origFilename.substring(pos+1); //.뒤의 확장자 반환
    }

}