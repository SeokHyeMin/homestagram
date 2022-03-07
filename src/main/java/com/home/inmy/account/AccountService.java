package com.home.inmy.account;

import com.home.inmy.domain.Profile;
import com.home.inmy.domain.Account;
import com.home.inmy.settings.form.AccountForm;
import com.home.inmy.settings.form.ProfileForm;
import com.home.inmy.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public Account processNewAccount(SignUpForm signUpForm){
        Account newAccount = saveNewAccount(signUpForm);
        return newAccount;
    }

    private Account saveNewAccount(SignUpForm signUpForm) {

        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);
        account.generateEmailToken();
        return accountRepository.save(account);
    }


    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Account account = accountRepository.findByLoginId(loginId);
        if(account == null){
            throw new UsernameNotFoundException(loginId);
        }

        return new UserAccount(account);
    }

    public void login(Account account) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public void updateProfile(Account account, ProfileForm profileForm) {

        account.setProfile(new Profile(profileForm.getBio(), profileForm.getUrl(), profileForm.getImage()));
        accountRepository.save(account);
    }

    public void updatePassword(Account account, String newPassword) {

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void updateNickname(Account account, String newNickname) {

        account.setNickname(newNickname);
        accountRepository.save(account);

    }

    public void updateLoginId(Account account, String newLoginId) {

        account.setLoginId(newLoginId);
        accountRepository.save(account);
    }

    public void updatePhoneNumber(Account account, String newPhoneNumber) {

        account.setPhoneNumber(newPhoneNumber);
        accountRepository.save(account);
    }

    public void updateAccount(Account account, AccountForm accountForm) {

        account.setNickname(accountForm.getNickname());
        account.setPhoneNumber(accountForm.getPhoneNumber());
        account.setEmail(accountForm.getEmail());
        account.setLoginId(accountForm.getLoginId());
        accountRepository.save(account);

    }

}
