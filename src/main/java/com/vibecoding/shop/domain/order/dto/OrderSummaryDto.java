package com.vibecoding.shop.domain.order.dto;

import com.vibecoding.shop.domain.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/** PART 3.6 — CQRS: 읽기 전용 DTO */
@Getter @AllArgsConstructor
public class OrderSummaryDto {
    private Long orderId;
    private OrderStatus status;
    private int totalAmount;
    private LocalDateTime createdAt;
}
