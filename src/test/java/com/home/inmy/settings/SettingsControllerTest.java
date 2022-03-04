package com.home.inmy.settings;

import com.home.inmy.WithAccount;
import com.home.inmy.account.AccountRepository;
import com.home.inmy.account.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired MockMvc mvc;

    @Autowired AccountRepository accountRepository;

    @Autowired PasswordEncoder passwordEncoder;

    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
    }

    @WithAccount("testId")
    @DisplayName("프로필 수정 폼 조회")
    @Test
    public void 프로필_수정_폼_조회() throws Exception {

        mvc.perform(get("/settings/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"));
    }


    @WithAccount("testId")
    @DisplayName("프로필 수정 성공")
    @Test
    public void 프로필_수정_성공() throws Exception{

        String bio = "안녕하세요. 짧은 소개입니다.";
        String url = "https://testurl.com";


        mvc.perform(post("/settings/profile")
                .param("bio",bio)
                .param("url",url)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("message"));

        Account testId = accountRepository.findByLoginId("testId");
        assertEquals(bio, testId.getProfile().getBio());

    }

    @WithAccount("testId")
    @DisplayName("프로필 수정 실패")
    @Test
    public void 프로필_수정_실패() throws Exception{

        String bio = "안녕하세요. 너무 긴 소개입니다. 너무 긴 소개입니다. 너무 긴 소개입니다. 너무 긴 소개입니다. 너무 긴 소개입니다. 너무 긴 소개입니다. 너무 긴 소개입니다." +
                "너무 긴 소개입니다." +
                "너무 긴 소개입니다. 너무 긴 소개입니다. 너무 긴 소개입니다. 너무 긴 소개입니다. 너무 긴 소개입니다.";

        mvc.perform(post("/settings/profile")
                        .param("bio",bio)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/profile"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profileForm"))
                .andExpect(model().hasErrors());
    }

    @WithAccount("testId")
    @DisplayName("비밀번호 수정 폼 조회")
    @Test
    public void 비밀번호_수정_폼_조회() throws Exception{

        mvc.perform(get("/settings/password"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount("testId")
    @DisplayName("비밀번호 수정 성공")
    @Test
    public void 비밀번호_수정_성공() throws Exception{

        mvc.perform(post("/settings/password")
                .param("newPassword","password123")
                .param("newPasswordConfirm","password123")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/password"))
                .andExpect(flash().attributeExists("message"));

        Account account = accountRepository.findByLoginId("testId");
        assertTrue(passwordEncoder.matches("password123",account.getPassword()));

    }

    @WithAccount("testId")
    @DisplayName("비밀번호 수정 실패")
    @Test
    public void 비밀번호_수정_실패() throws Exception{

        mvc.perform(post("/settings/password")
                        .param("newPassword","password123")
                        .param("newPasswordConfirm","password321")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/password"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"));
    }

    @WithAccount("testId")
    @DisplayName("계정 수정 폼 조회")
    @Test
    public void 계정_수정_폼() throws Exception{

        mvc.perform(get("/settings/account"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("accountForm"));
    }

    @WithAccount("testId")
    @DisplayName("계정 수정 성공")
    @Test
    public void 계정_수정_성공() throws Exception{

        mvc.perform(post("/settings/account")
                .param("nickname", "nickNew")
                .param("email","newEmail@email.com")
                .param("loginId","newTestId")
                .param("phoneNumber","01088997766")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/account"))
                .andExpect(flash().attributeExists("message"));
    }



}