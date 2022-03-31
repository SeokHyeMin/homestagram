package com.home.inmy.service.impl;

import com.home.inmy.domain.UserAccount;
import com.home.inmy.domain.entity.Profile;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Role;
import com.home.inmy.repository.AccountRepository;
import com.home.inmy.form.AccountForm;
import com.home.inmy.form.ProfileForm;
import com.home.inmy.form.SignUpForm;
import com.home.inmy.service.AccountService;
import com.home.inmy.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;


    @Override
    public Account createAccount(SignUpForm signUpForm) {
        return saveNewAccount(signUpForm);
    }

    @Override
    public void deleteAccount(Long account_id) {
        accountRepository.deleteById(account_id);
    }


    private Account saveNewAccount(SignUpForm signUpForm) {

        //비밀번호 암호화하여 저장.
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);

        //권한 부여, 기본회원가입의 경우 USER
        Role role = roleService.createRoleIfNotFound("ROLE_USER", "사용자");
        account.setAccountRole(role);

        //현재 시간으로 가입일 지정
        account.setJoinedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    public void login(Account account) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Override
    public void updateProfile(Account account, ProfileForm profileForm) {

        account.setProfile(new Profile(profileForm.getBio(), profileForm.getUrl(), profileForm.getImage()));
        accountRepository.save(account);
    }

    @Override
    public void updatePassword(Account account, String newPassword) {

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Override
    public void updateAccount(Account account, AccountForm accountForm) {

        account.setNickname(accountForm.getNickname());
        account.setPhoneNumber(accountForm.getPhoneNumber());
        account.setEmail(accountForm.getEmail());
        account.setLoginId(accountForm.getLoginId());
        accountRepository.save(account);

    }

    public Account getAccount(String loginId) {return accountRepository.findByLoginId(loginId);}

    public Page<Account> getAccountList(int page){
        PageRequest pageRequest = PageRequest.of(page, 5);
        return accountRepository.findAll(pageRequest);
    }


}
