package com.home.inmy.settings.form;

import com.home.inmy.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class LoginIdForm {

    @Length(min = 5, max = 10)
    private String loginId;

    public LoginIdForm(Account account){
        this.loginId = account.getLoginId();
    }
}
