package com.vibecoding.shop.domain.member.adapter.in.web;

import com.vibecoding.shop.common.response.ApiResponse;
import com.vibecoding.shop.domain.member.application.MemberService;
import com.vibecoding.shop.domain.member.dto.SignUpCommand;
import com.vibecoding.shop.domain.member.dto.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 API")
@RestController @RequestMapping("/api/v1") @RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입")
    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MemberResponse> signUp(@Valid @RequestBody SignUpCommand command) {
        return ApiResponse.success("회원 가입이 완료되었습니다.", memberService.signUp(command));
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping("/members/me")
    public ApiResponse<MemberResponse> getMe(@AuthenticationPrincipal Long memberId) {
        return ApiResponse.success(memberService.getMember(memberId));
    }
}
