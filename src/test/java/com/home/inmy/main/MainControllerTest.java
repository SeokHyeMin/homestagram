package com.home.inmy.main;

import com.home.inmy.repository.AccountRepository;
import com.home.inmy.service.impl.AccountServiceImpl;
import com.home.inmy.form.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    AccountRepository accountRepository;


    @BeforeEach
    void beforeEach(){ //자주 사용할 것 같은 부분을 선언해서 매 테스트 이전에 반복적으로 수행하도록 설정
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setLoginId("testId");
        signUpForm.setPassword("testPassword");
        signUpForm.setPhoneNumber("01012345678");
        signUpForm.setEmail("test@naver.com");
        signUpForm.setNickname("nick");
        signUpForm.setDateToBirth("20220217");
        accountService.createAccount(signUpForm);
    }

    @AfterEach()
    void AfterEach(){ //다음 테스트에 영향을 미치지 않도록, DB의 데이터 지워주기
        accountRepository.deleteAll();
    }

    @DisplayName("로그인 성공")
    @Test
    public void 로그인_성공() throws Exception{

        mvc.perform(post("/login")
                .param("loginId","testId")
                .param("password","testPassword")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("nick"));
    }

    @DisplayName("로그인 실패")
    @Test
    public void 로그인_실패() throws Exception{

        mvc.perform(post("/login")
                        .param("loginId","testIdError")
                        .param("password","testPassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error")) //실패시에 spring security가 자동으로 처리해줌.
                .andExpect(unauthenticated());
    }

}
