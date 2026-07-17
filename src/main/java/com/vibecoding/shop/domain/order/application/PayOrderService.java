package com.vibecoding.shop.domain.order.application;

import com.vibecoding.shop.domain.order.dto.PayOrderCommand;
import com.vibecoding.shop.domain.order.port.in.PayOrderUseCase;
import com.vibecoding.shop.domain.order.port.out.EventPublisherPort;
import com.vibecoding.shop.domain.order.port.out.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PART 8.5 — 이벤트 기반: 결제 완료 후 도메인 이벤트 발행
 * → PointEventListener: 포인트 적립
 * → EmailEventListener: 주문 확인 이메일
 * → InventoryEventListener: 재고 확정
 */
@Service @Transactional @RequiredArgsConstructor @Slf4j
public class PayOrderService implements PayOrderUseCase {

    private final OrderRepository    orderRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    public void payOrder(PayOrderCommand command) {
        var order = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new IllegalStateException("주문을 찾을 수 없습니다."));

        order.pay(); // 도메인 메서드: 상태 변경 + OrderPaidEvent 등록
        orderRepository.save(order); // 저장 시 도메인 이벤트 자동 발행

        // Outbox에도 저장 (이벤트 유실 방지)
        eventPublisher.publish("ORDER", order.getId().toString(),
                "ORDER_PAID", new OrderPaidPayload(order.getId(), command.getMemberId()));

        log.info("결제 완료 처리. orderId={}", command.getOrderId());
    }

    record OrderPaidPayload(Long orderId, Long memberId) {}
}
