package com.home.inmy.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedEntityGraph(name = "Comment.withAccount", attributeNodes = {
        @NamedAttributeNode("account"),
})
@Entity
@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Comments {

    @Id @GeneratedValue
    @Column(name = "comments_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String comment;

    private LocalDateTime writeTime;

}
