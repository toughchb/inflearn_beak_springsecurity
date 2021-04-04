package com.example.demospringsecurity.config;

import com.example.demospringsecurity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

/*
    @Autowired
    AccountService accountService;
*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /***
         *  /, /info 요청은 모든 사용자에게
         *  admin 요청은 ADMIN 권한이 있을때만
         *  아직 Spring Security 에서 제공해주는 기본 user로만 로그인이 가능하다
         */
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**").permitAll()
                .mvcMatchers("admin").hasRole("ADMIN")
                .anyRequest().authenticated();
        http.formLogin();
        http.httpBasic();
    }

    /***
     * 원래는 이런식으로 WebSecurityConfigurerAdapter 에게 AccountService를 명시해줘서 사용하고있음을 알려줘도 되는데
     * UserDetailsService의 구현체가 빈으로 등록만 되어있어도 알아서 사용함
     */
/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService);
    }*/
}
