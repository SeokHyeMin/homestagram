package com.home.inmy.security;

import com.home.inmy.domain.UserAccount;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginId(loginId);

        if (account == null) {
            throw new UsernameNotFoundException(loginId);
        }

        return new UserAccount(account);
    }
}
