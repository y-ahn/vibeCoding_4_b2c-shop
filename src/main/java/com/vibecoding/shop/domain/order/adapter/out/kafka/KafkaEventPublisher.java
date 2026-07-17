package com.vibecoding.shop.domain.order.adapter.out.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibecoding.shop.domain.order.port.out.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** PART 5.4 — Outbox 패턴 + Kafka 발행 */
@Component @RequiredArgsConstructor @Slf4j
public class KafkaEventPublisher implements EventPublisherPort {

    private final OutboxEventRepository outboxRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void publish(String aggregateType, String aggregateId,
                        String eventType, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            outboxRepo.save(OutboxEventEntity.of(aggregateType, aggregateId, eventType, json));
        } catch (Exception e) {
            log.error("Outbox 이벤트 저장 실패: {}", e.getMessage());
        }
    }

    /** 1초마다 미발행 이벤트를 Kafka로 발행 */
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEventEntity> pending = outboxRepo.findByPublishedFalse();
        for (OutboxEventEntity event : pending) {
            try {
                kafkaTemplate.send(event.getEventType(), event.getAggregateId(), event.getPayload());
                event.markPublished();
                log.debug("Kafka 발행 완료: type={}, id={}", event.getEventType(), event.getAggregateId());
            } catch (Exception e) {
                log.error("Kafka 발행 실패: {}", e.getMessage());
            }
        }
    }
}
