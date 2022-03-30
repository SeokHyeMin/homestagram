package com.home.inmy.config;

import com.home.inmy.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import com.home.inmy.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring() //보안필터를 적용할 필요가 없는 리소스 설정.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/h2-console/**");
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/","/login*","/sign-up","check-email-token","/post-list","/post-detail").permitAll()
                .mvcMatchers(HttpMethod.GET,"/profile/**").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.formLogin() //로그인 인증 기능
                .loginPage("/login") //로그인 페이지
                .usernameParameter("loginId") //loginId로 파라미터 변경
                .defaultSuccessUrl("/")
                .permitAll();

        http.logout()
                .logoutSuccessUrl("/");

        http.rememberMe() //로그인 유지 관한 설정
                .tokenValiditySeconds(3600) //로그인 유지 시간 1시간으로 설정
                .alwaysRemember(false)
                .userDetailsService(userDetailsService);

        http.sessionManagement()
                .maximumSessions(1) //최대 허용 가능한 세션 수 : 1
                .maxSessionsPreventsLogin(false) //동시 로그인한 경우 기존(이전)의 세션을 만료시킴
                .expiredUrl("/expired"); //세션이 만료된 경우 이동할 페이지지

        //필터설정
        http.addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class); //기존에 있던 필터보다 먼저 실행된다.
    }
    /*@Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() { //기본 인코더 등록.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {

        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased()); //접근결정 관리자, 하나만 승인되도 되는 방식으로 설정
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean()); //인증관리자

        return filterSecurityInterceptor;
    }

    private AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        return Arrays.asList(new RoleVoter());
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
        return new UrlFilterInvocationSecurityMetadataSource();
    }

}
