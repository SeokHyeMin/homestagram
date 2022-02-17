package com.home.inmy.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 5, max = 10)
    @Pattern(regexp = "^[A-Za-z0-9]{5,10}$")
    private String loginId;

    @NotBlank
    @Length(min = 2, max = 10)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9]{2,10}$")
    private String nickname;

    @NotBlank
    private String name;

    @NotBlank
    @Length(min = 8, max = 8)
    private String dateToBirth;

    @NotBlank
    @Length(min = 8, max = 15)
    private String password;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10,11}$")
    private String phoneNumber;

}
