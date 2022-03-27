package com.home.inmy.account;

import com.home.inmy.domain.Profile;
import com.home.inmy.domain.Account;
import com.home.inmy.settings.form.AccountForm;
import com.home.inmy.settings.form.ProfileForm;
import com.home.inmy.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public Account processNewAccount(SignUpForm signUpForm){
        return saveNewAccount(signUpForm);
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

    public void updateAccount(Account account, AccountForm accountForm) {

        account.setNickname(accountForm.getNickname());
        account.setPhoneNumber(accountForm.getPhoneNumber());
        account.setEmail(accountForm.getEmail());
        account.setLoginId(accountForm.getLoginId());
        accountRepository.save(account);

    }

    public Account getAccount(String loginId) {

        Account account = accountRepository.findByLoginId(loginId);

        if (account == null) {
            throw new IllegalArgumentException(loginId + "에 해당하는 사용자가 없습니다.");
        }

        return account;
    }

    public void deleteAccount(Long account_id) {
        accountRepository.deleteById(account_id);
    }

    public Page<Account> getAccountList(int page){
        PageRequest pageRequest = PageRequest.of(page, 5);
        return accountRepository.findAll(pageRequest);
    }
}
