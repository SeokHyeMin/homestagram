package com.home.inmy.account;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired private AccountRepository accountRepository;

    @DisplayName("회원가입 화면 확인")
    @Test
    public void 회원가입_화면_확인() throws Exception{

        mvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원가입 정상")
    @Test
    public void 회원가입_정상() throws Exception{

       mvc.perform(post("/sign-up")
               .param("name","테스트")
               .param("dateToBirth","20220212")
               .param("loginId","test1")
               .param("password","test1234")
                       .with(csrf())
               .param("nickname","nick")
               .param("email","test@email.com")
               .param("phoneNumber","01012345678"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/"))
               .andExpect(authenticated().withUsername("test1"));

        Assertions.assertTrue(accountRepository.existsByEmail("test@email.com"));
        Account account = accountRepository.findByLoginId("test1");
        assertNotEquals(account.getPassword(), "test1234");  //가입 시 설정한 비밀번호와 인코더 된 값이 달라야 정상.
    }

    @DisplayName("회원가입 실패 - 입력값 오류")
    @Test
    public void 회원가입_실패() throws Exception{

        mvc.perform(post("/sign-up")
                        .param("name","테스트")
                        .param("dateToBirth","20220212")
                        .with(csrf())
                        .param("loginId","test")
                        .param("password","tes")
                        .param("nickname","nickname")
                        .param("email","test@mail")
                        .param("phoneNumber","01012345678"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up")) //입력값 오류여도 다시 페이지로 돌아가는지 테스트
                .andExpect(unauthenticated());

    }
}