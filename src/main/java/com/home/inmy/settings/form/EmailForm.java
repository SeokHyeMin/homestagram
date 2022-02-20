package com.home.inmy.settings.form;

import com.home.inmy.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
public class EmailForm {

    @Email
    private String email;

    public EmailForm(Account account){
        this.email = account.getEmail();
    }
}
