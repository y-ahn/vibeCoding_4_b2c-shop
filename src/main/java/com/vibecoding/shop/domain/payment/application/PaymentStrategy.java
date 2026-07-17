package com.vibecoding.shop.domain.payment.application;

/** PART 4.3 — 전략 패턴: 결제 수단별 처리 */
public interface PaymentStrategy {
    String getPaymentMethod();
    String process(Long orderId, int amount);  // PG사 거래 ID 반환
}
