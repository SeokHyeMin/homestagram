package com.home.inmy;


import com.home.inmy.security.service.CustomUserDetailsService;
import com.home.inmy.service.impl.AccountServiceImpl;
import com.home.inmy.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountServiceImpl accountService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {

        String loginId = withAccount.value();

        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setLoginId(loginId);
        signUpForm.setEmail("test@email.com");
        signUpForm.setNickname("nickHi");
        signUpForm.setPassword("test1234");
        signUpForm.setDateToBirth("20220218");
        signUpForm.setPhoneNumber("01012345678");
        signUpForm.setName("테스트");
        accountService.createAccount(signUpForm);

        UserDetails principal = customUserDetailsService.loadUserByUsername(loginId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
