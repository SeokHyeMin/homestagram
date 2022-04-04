package com.home.inmy.service;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.form.AccountForm;
import com.home.inmy.form.ProfileForm;
import com.home.inmy.form.SignUpForm;
import org.springframework.data.domain.Page;


public interface AccountService{

    void createAccount(SignUpForm signUpForm);

    void updateAccount(Account account, AccountForm accountForm);

    void updatePassword(Account account, String newPassword);

    void updateProfile(Account account, ProfileForm profileForm);

    void deleteAccount(Long idx);

    Account getAccount(String loginId);

    Page<Account> getAccountList(int page);

    void updateAccountRole(Long id, String roleName);

    void findPassword(Account account);
}
