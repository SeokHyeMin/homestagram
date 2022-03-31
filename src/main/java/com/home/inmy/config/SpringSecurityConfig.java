package com.home.inmy.config;

import com.home.inmy.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService); //인증처리
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring() //보안필터를 적용할 필요가 없는 리소스 설정.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .mvcMatchers("/node_modules/**","/image/**")
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/","/login*","/sign-up","/postList","/post-detail").permitAll()
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
                .alwaysRemember(false);

        http.sessionManagement()
                .maximumSessions(1) //최대 허용 가능한 세션 수 : 1
                .maxSessionsPreventsLogin(false) //동시 로그인한 경우 기존(이전)의 세션을 만료시킴
                .expiredUrl("/expired"); //세션이 만료된 경우 이동할 페이지지
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //기본 인코더 등록.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
