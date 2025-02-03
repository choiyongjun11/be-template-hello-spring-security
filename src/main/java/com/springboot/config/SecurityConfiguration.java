package com.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    //보안 구성 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable() //끄는 기능
                .formLogin() //form 로그인 방식을 사용합니다.
                .loginPage("/auths/login-form") //get 요청을 보냅니다.
                .loginProcessingUrl("/process_login")
                .failureUrl("/auths/login-form?error") //로그인이 실패 했을때 넘기는 것 권한이 없을 때
                .and()
                .logout() //로그아웃
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()

                .exceptionHandling().accessDeniedPage("/auths/access-denied")
                .and()
                .authorizeRequests(authorzie -> authorzie
                        .antMatchers("/orders/**").hasRole("ADMIN") //모든 하위 풀더에 admin 권한을 가진 놈들
                        .antMatchers("/members/my-page").hasRole("USER") //members의 user 권한을 가진 놈들
                        .antMatchers("/**").permitAll() //전부 허용합니다.

                ); //권한 인가 부분

        return http.build();
    }

    // 회원 생성 코드
//    @Bean
//    public UserDetailsManager userDetailsManager() {
//        UserDetails userDetails = User.withDefaultPasswordEncoder()
//                .username("lucky@cat.house")
//                .password("1234")
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin@google.com")
//                .password("qwer")
//                .roles("ADMIN") //마이페이지는 접근이 안됩니다.
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails, admin);
//
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
