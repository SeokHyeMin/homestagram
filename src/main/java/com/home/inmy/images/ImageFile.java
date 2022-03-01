package com.home.inmy.images;

import com.home.inmy.domain.Post;
import lombok.*;

import javax.persistence.*;

@Embeddable
@Data
@NoArgsConstructor
public class ImageFile { //업로드한 파일정보를 보관.

    private String origFilename;  // 업로드한 파일 원본명

    private String storeFilename; //같은 파일이름이 있으면 충돌이 날 수 있으므로, 파일명 겹치치 않도록 관리할 별도의의 파일

    public ImageFile(String origFilename, String storeFilename){
        this.origFilename = origFilename;
        this.storeFilename = storeFilename;
    }

}
