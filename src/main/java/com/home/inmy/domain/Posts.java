package com.home.inmy.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Posts {

    @Id @GeneratedValue
    private Long post_num;

    @Length(min = 2, max = 20)
    private String title;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    private String tag;

    private String content;

    private String author;

    private LocalDateTime writeTime;

}
