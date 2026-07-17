package com.vibecoding.shop.domain.order.port.in;

public interface CancelOrderUseCase {
    void cancelOrder(Long orderId, Long memberId);
}
