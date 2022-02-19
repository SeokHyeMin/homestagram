package com.home.inmy.account.form;

import com.home.inmy.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class ProfileForm {

    @Length(max = 35)
    private String bio;

    @Length(max = 30)
    private String job;

    @Length(max = 50)
    private String url;

    private String image;

    public ProfileForm(Account account){
        this.bio = account.getProfile().getBio();
        this.job = account.getProfile().getJob();
        this.url = account.getProfile().getUrl();
        this.image = account.getProfile().getImage();
    }
}
