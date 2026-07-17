package com.vibecoding.shop.domain.order.entity;

/** PART 5.3 — 도메인 이벤트: 결제 완료 */
public record OrderPaidEvent(Long orderId, Long memberId, Money totalAmount) {}
