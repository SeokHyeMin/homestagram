package com.home.inmy.settings.form;

import com.home.inmy.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class NicknameForm {

    @Length(min = 2, max = 10)
    private String nickname;

    public NicknameForm(Account account){
        this.nickname = account.getNickname();
    }
}
