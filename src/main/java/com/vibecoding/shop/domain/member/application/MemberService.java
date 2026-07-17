package com.vibecoding.shop.domain.member.application;

import com.vibecoding.shop.domain.member.dto.SignUpCommand;
import com.vibecoding.shop.domain.member.dto.MemberResponse;
import com.vibecoding.shop.domain.member.entity.Member;
import com.vibecoding.shop.domain.member.adapter.out.persistence.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class MemberService {

    private final MemberJpaRepository memberRepo;
    private final PasswordEncoder     passwordEncoder;

    public MemberResponse signUp(SignUpCommand command) {
        if (memberRepo.existsByEmail(command.email()))
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");

        Member member = Member.create(
            command.email(),
            passwordEncoder.encode(command.password()),
            command.name());

        Member saved = memberRepo.save(member);
        log.info("회원가입 완료. memberId={}", saved.getId());
        return MemberResponse.from(saved);
    }

    @Cacheable(value = "member", key = "#memberId")
    @Transactional(readOnly = true)
    public MemberResponse getMember(Long memberId) {
        Member member = memberRepo.findById(memberId)
            .orElseThrow(() -> new IllegalStateException("회원을 찾을 수 없습니다."));
        return MemberResponse.from(member);
    }

    /** 포인트 적립 (OrderPaidEvent 리스너에서 호출) */
    public void earnPoint(Long memberId, int amount) {
        memberRepo.findById(memberId).ifPresent(m -> {
            m.earnPoint(amount);
            log.info("포인트 적립. memberId={}, amount={}", memberId, amount);
        });
    }
}
