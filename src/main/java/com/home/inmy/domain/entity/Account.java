package com.home.inmy.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@Builder @AllArgsConstructor() @NoArgsConstructor()
public class Account {

    //회원가입
    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String nickname;

    private String dateToBirth;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    @Column(unique = true)
    private String loginId;

    @Column(unique = true)
    private String phoneNumber;

    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.REMOVE)
    private List<Follow> followList = new ArrayList<>();

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.REMOVE)
    private List<Follow> followerList = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Comments> commentsList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role accountRole;

    //프로필
    @Embedded
    private Profile profile = new Profile();

    public Profile getProfile(){
        return this.profile == null ? new Profile() : this.profile;
    }
}
