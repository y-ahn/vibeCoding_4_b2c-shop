package com.vibecoding.shop.domain.payment.application;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component("KAKAO_PAY")
public class KakaoPayStrategy implements PaymentStrategy {
    @Override public String getPaymentMethod() { return "KAKAO_PAY"; }
    @Override public String process(Long orderId, int amount) {
        return "KAKAO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
