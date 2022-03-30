package com.home.inmy.service;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.dto.AccountDto;
import com.home.inmy.form.AccountForm;
import com.home.inmy.form.ProfileForm;
import com.home.inmy.form.SignUpForm;


public interface AccountService {

    Account createAccount(SignUpForm signUpForm);

    void updateAccount(Account account, AccountForm accountForm);

    void updatePassword(Account account, String newPassword);

    void updateProfile(Account account, ProfileForm profileForm);

    void deleteAccount(Long idx);

}
