package com.vibecoding.shop.domain.order.application;

import com.vibecoding.shop.domain.order.port.in.CancelOrderUseCase;
import com.vibecoding.shop.domain.order.port.out.EventPublisherPort;
import com.vibecoding.shop.domain.order.port.out.OrderRepository;
import com.vibecoding.shop.domain.order.port.out.ProductStockPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepository    orderRepository;
    private final ProductStockPort   productStockPort;
    private final EventPublisherPort eventPublisher;

    @Override
    public void cancelOrder(Long orderId, Long memberId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("주문을 찾을 수 없습니다."));

        order.cancel(); // 도메인 메서드 — 상태 검증 + 취소 처리

        // 재고 복구 (보상 트랜잭션 — PART 6.6 Saga 패턴)
        order.getOrderLines().forEach(line ->
            productStockPort.restoreStock(line.getProductId(), line.getQuantity()));

        orderRepository.save(order);

        eventPublisher.publish("ORDER", orderId.toString(),
                "ORDER_CANCELLED", new OrderCancelledPayload(orderId, memberId));

        log.info("주문 취소 완료. orderId={}, memberId={}", orderId, memberId);
    }

    record OrderCancelledPayload(Long orderId, Long memberId) {}
}
