package com.home.inmy.settings.form;

import com.home.inmy.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class ProfileForm {

    @Length(max = 35)
    private String bio;

    @Length(max = 50)
    private String url;

    private String image;

    public ProfileForm(Account account){
        this.bio = account.getProfile().getBio();
        this.url = account.getProfile().getUrl();
        this.image = account.getProfile().getImage();
    }
}
