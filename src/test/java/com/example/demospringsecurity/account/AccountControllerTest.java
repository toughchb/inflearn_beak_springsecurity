package com.example.demospringsecurity.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;

     @Test
     @WithAnonymousUser
    public void index_anonymous() throws Exception {
         mockMvc.perform(get("/"))
                 .andDo(print())
                 .andExpect(status().isOk());
     }

    @Test
    @WithMockUser(username = "hbchae", roles = "USER")
    public void index_user() throws Exception {//hbchae 라는 유저가 있다고 가정하고
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void index_admin() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "hbchae", roles = "USER")
    public void admin_user() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void admin_admin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional /* createUser하며 데이터 변경 사항을 롤백 하기 위해 */
    public void login_success() throws Exception {
        Account user = this.createUser();
        mockMvc.perform(formLogin().user(user.getUsername()).password("123"))
                 .andExpect(authenticated());
    }

    @Test
    @Transactional
    public void login_fail() throws Exception {
        Account user = this.createUser();
        mockMvc.perform(formLogin().user(user.getUsername()).password("1234"))
                .andExpect(unauthenticated());
    }

    private Account createUser() {
        Account account = new Account();
        account.setUsername("hbchae");
        account.setPassword("123");
        account.setRole("USER");
        return accountService.createNew(account);
    }
}