package com.vibecoding.shop.domain.payment.application;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component("CARD")
public class CardPaymentStrategy implements PaymentStrategy {
    @Override public String getPaymentMethod() { return "CARD"; }
    @Override public String process(Long orderId, int amount) {
        // 실제 카드 PG사 연동 로직 (가상)
        return "CARD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
