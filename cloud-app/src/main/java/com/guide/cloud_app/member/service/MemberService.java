package com.guide.cloud_app.member.service;

import com.guide.cloud_app.member.entity.Member;
import com.guide.cloud_app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member signup(String email, String name, String password) {
        Member member = Member.of(email, name, password);
        return memberRepository.save(member);
    }

    public Member login(String email, String password) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Illegal Request");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with email: " + email));

        if (!password.equals(member.getPassword())) {
            throw new IllegalArgumentException("Member not found with email: " + email);
        }

        return member;
    }

    public Member getMember(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("Member Id is null");
        }
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
    }

    public void validateMember(Long memberId) {
        if (memberId == null || !memberRepository.existsById(memberId)) {
            throw new IllegalArgumentException("Member not found with id: " + memberId);
        }
    }
}
