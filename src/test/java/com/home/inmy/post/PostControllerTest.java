package com.home.inmy.post;

import com.home.inmy.WithAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired MockMvc mvc;

    @DisplayName("게시글 등록 화면")
    @Test
    @WithAccount("testId")
    public void 게시글_등록_화면() throws Exception{

        mvc.perform(get("/new-post"))
                .andExpect(model().attributeExists("postDto"))
                .andExpect(status().isOk());
    }

    @DisplayName("게시글 작성 성공")
    @Test
    @WithAccount("testId")
    public void 게시글_작성_성공() throws Exception{


        mvc.perform(post("/new-post")
                .param("title","게시글 제목")
                .param("tag","일단 임시태그")
                .param("content","내용입니다.")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }



}