package com.vibecoding.shop.domain.order.entity;

/** PART 4.5 — 상태 패턴 적용 대상 */
public enum OrderStatus {
    PAYMENT_WAITING,  // 결제 대기
    PREPARING,        // 상품 준비 중
    SHIPPED,          // 배송 중
    DELIVERED,        // 배송 완료
    CANCELLED         // 취소
}
