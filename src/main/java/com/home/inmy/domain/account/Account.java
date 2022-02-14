package com.home.inmy.domain.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class Account {

    //회원가입
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nickname;

    private int dateToBirth;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    @Column(unique = true)
    private String loginId;

    @Column(unique = true)
    private String phoneNumber;

    //이메일 체크
    private String emailCheckVerified;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedAt;



    //프로필
    private String bio;


}
