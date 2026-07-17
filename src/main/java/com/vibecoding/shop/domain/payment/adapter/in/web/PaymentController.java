package com.vibecoding.shop.domain.payment.adapter.in.web;

import com.vibecoding.shop.common.response.ApiResponse;
import com.vibecoding.shop.domain.payment.application.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment", description = "결제 API")
@RestController @RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 처리")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> pay(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody PaymentRequest request) {
        Long paymentId = paymentService.pay(
            request.getOrderId(), memberId,
            request.getAmount(), request.getPaymentMethod());
        return ApiResponse.success("결제가 완료되었습니다.", paymentId);
    }

    @Operation(summary = "환불")
    @DeleteMapping("/{paymentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refund(@PathVariable Long paymentId) {
        paymentService.refund(paymentId);
    }

    @Getter @NoArgsConstructor
    static class PaymentRequest {
        @NotNull  private Long   orderId;
        @NotNull  private int    amount;
        @NotBlank private String paymentMethod; // CARD, KAKAO_PAY, NAVER_PAY, TOSS
    }
}
