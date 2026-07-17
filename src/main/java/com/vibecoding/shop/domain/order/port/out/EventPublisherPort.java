package com.vibecoding.shop.domain.order.port.out;

/** PART 5.4 — Outbox 패턴: 이벤트 발행 Outbound Port */
public interface EventPublisherPort {
    void publish(String aggregateType, String aggregateId,
                 String eventType, Object payload);
}
