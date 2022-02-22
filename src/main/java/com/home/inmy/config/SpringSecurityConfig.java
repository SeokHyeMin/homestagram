package com.home.inmy.config;

import com.home.inmy.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/","/login","/sign-up","check-email-token").permitAll()
                .mvcMatchers(HttpMethod.GET,"/profile/*").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .usernameParameter("loginId") //username이 아닌 loginId로 파라미터 변경
                .permitAll();

        http.logout()
                .logoutSuccessUrl("/");


        http.rememberMe() //로그인 유지 관한 설정
               .userDetailsService(accountService)
                .tokenRepository(tokenRepository());
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring() //static은 검증없이 접근가능하도록.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /* AuthenticationManager Bean 등록 */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
