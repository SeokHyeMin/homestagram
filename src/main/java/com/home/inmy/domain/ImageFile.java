package com.home.inmy.domain;

import com.home.inmy.domain.Post;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ImageFile { //업로드한 파일정보를 보관.

    @Id @GeneratedValue
    private Long id;

    private String origFilename;  // 업로드한 파일 원본명

    private String storeFilename; //같은 파일이름이 있으면 충돌이 날 수 있으므로, 파일명 겹치치 않도록 관리할 별도의의 파일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public ImageFile(String origFilename, String storeFilename, Post post){
        this.origFilename = origFilename;
        this.storeFilename = storeFilename;
        this.post = post;
    }


}
