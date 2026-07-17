package com.vibecoding.shop.domain.notification.application;

import com.vibecoding.shop.domain.order.entity.OrderCancelledEvent;
import com.vibecoding.shop.domain.order.entity.OrderPaidEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * PART 5.3 — 도메인 이벤트 리스너
 * @TransactionalEventListener: 트랜잭션 커밋 후 실행
 * @Async: 비동기 처리 (이메일 발송이 응답 속도에 영향 없음)
 */
@Component @RequiredArgsConstructor @Slf4j
public class OrderEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderPaid(OrderPaidEvent event) {
        log.info("[이벤트] 결제 완료 → 이메일 발송. orderId={}, memberId={}",
                event.orderId(), event.memberId());
        // 실제: emailService.sendOrderConfirmation(event.memberId(), event.orderId())
        // 실제: pointService.earn(event.memberId(), calculatePoints(event.totalAmount()))
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderCancelled(OrderCancelledEvent event) {
        log.info("[이벤트] 주문 취소 → 취소 알림 발송. orderId={}", event.orderId());
        // 실제: emailService.sendCancelNotification(event.memberId(), event.orderId())
    }
}
