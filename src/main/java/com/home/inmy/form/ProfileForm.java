package com.home.inmy.form;

import com.home.inmy.domain.entity.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class ProfileForm {

    @Length(max = 18)
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
