package com.home.inmy.account;

import com.home.inmy.account.form.SignUpForm;
import com.home.inmy.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

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
}
