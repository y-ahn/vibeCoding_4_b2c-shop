package com.vibecoding.shop.domain.order.adapter.out.kafka;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/** PART 5.4 — Outbox 패턴: 이벤트 유실 방지 */
@Entity @Table(name = "outbox_events")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEventEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_type") private String aggregateType;
    @Column(name = "aggregate_id")   private String aggregateId;
    @Column(name = "event_type")     private String eventType;
    @Lob                             private String payload;
    @Column                          private boolean published = false;
    @Column(name = "created_at")     private LocalDateTime createdAt = LocalDateTime.now();

    public static OutboxEventEntity of(String aggType, String aggId,
                                        String eventType, String payload) {
        OutboxEventEntity e = new OutboxEventEntity();
        e.aggregateType = aggType; e.aggregateId = aggId;
        e.eventType = eventType;   e.payload = payload;
        return e;
    }
    public void markPublished() { this.published = true; }
}
