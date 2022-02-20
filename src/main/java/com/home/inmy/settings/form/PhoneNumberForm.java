package com.home.inmy.settings.form;

import com.home.inmy.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Data
@NoArgsConstructor
public class PhoneNumberForm {


    @Length(min = 10, max = 11)
    private String phoneNumber;

    public PhoneNumberForm(Account account){
        this.phoneNumber = account.getPhoneNumber();
    }

}
