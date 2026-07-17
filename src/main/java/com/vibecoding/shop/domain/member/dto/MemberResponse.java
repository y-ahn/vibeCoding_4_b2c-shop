package com.vibecoding.shop.domain.member.dto;

import com.vibecoding.shop.domain.member.entity.Member;

public record MemberResponse(Long id, String email, String name, int point) {
    public static MemberResponse from(Member m) {
        return new MemberResponse(m.getId(), m.getEmail(), m.getName(), m.getPoint());
    }
}
