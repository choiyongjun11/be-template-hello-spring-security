package com.springboot.member;

import com.springboot.auth.utils.HelloAuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DBMemberService implements MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils helloAuthorityUtils;

    public DBMemberService(PasswordEncoder passwordEncoder, MemberRepository memberRepository, HelloAuthorityUtils helloAuthorityUtils) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.helloAuthorityUtils = helloAuthorityUtils;
    }

    @Override
    public Member createMember(Member member) {
//        verifyExistsEmail(member.getEmail());
        String encryptedPassword = passwordEncoder
                .encode(member.getPassword());
        member.setPassword(encryptedPassword);

        List<String> roles =
                helloAuthorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);


        Member savedMember = memberRepository.save(member);
        return savedMember;

    }
}
