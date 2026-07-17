package com.vibecoding.shop.domain.order.entity;

/** PART 5.3 — 도메인 이벤트: 주문 취소 */
public record OrderCancelledEvent(Long orderId, Long memberId) {}
