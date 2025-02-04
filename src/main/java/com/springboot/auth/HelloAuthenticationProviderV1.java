package com.springboot.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class HelloAuthenticationProviderV1 implements AuthenticationProvider {

    private final HelloUserDetailsServiceV2 userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public HelloAuthenticationProviderV1(HelloUserDetailsServiceV2 userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        String username = authToken.getName();
        Optional.ofNullable(username).orElseThrow(() -> new UsernameNotFoundException("Invalid User name or User Password"));

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //비밀번호로 검증 메서드
            verifyCredentials(authToken.getCredentials(), userDetails.getPassword());

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            return UsernamePasswordAuthenticationToken.authenticated(username, userDetails.getPassword(), authorities);

        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

    private void verifyCredentials(Object credentials, String password) {
        if(!passwordEncoder.matches((String) credentials, password)) {
            throw new BadCredentialsException("Invalid User name or User Password");
        }
    }

}
