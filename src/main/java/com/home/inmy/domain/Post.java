package com.home.inmy.domain;

import com.home.inmy.images.ImageFile;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Post {

    @Id @GeneratedValue
    private Long post_num;

    @Length(min = 2, max = 20)
    private String title;

    @ElementCollection
    private List<ImageFile> imageFiles = new ArrayList<ImageFile>();

    @OneToMany(mappedBy = "post")
    private Set<Post_Tag> post_tags = new HashSet<>();

    private String content;

    private String author;

    private LocalDateTime writeTime;

    private String category;

    public void completePostSave(){
        this.writeTime = LocalDateTime.now();
    }

}
