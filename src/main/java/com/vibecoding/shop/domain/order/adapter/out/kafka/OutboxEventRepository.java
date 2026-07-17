package com.vibecoding.shop.domain.order.adapter.out.kafka;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, Long> {
    List<OutboxEventEntity> findByPublishedFalse();
}
