package com.home.inmy.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
public class AccountForm {

    @Email
    private String email;

    @Length(min = 5, max = 10)
    private String loginId;

    @Length(min = 2, max = 10)
    private String nickname;

    @Length(min = 10, max = 11)
    private String phoneNumber;

}
