package com.vibecoding.shop.domain.order.dto;

import com.vibecoding.shop.domain.order.entity.Order;
import com.vibecoding.shop.domain.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter @AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long memberId;
    private OrderStatus status;
    private int totalAmount;
    private String currency;
    private LocalDateTime createdAt;

    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(), order.getMemberId(), order.getStatus(),
            order.getTotalAmount().getAmount(),
            order.getTotalAmount().getCurrency(),
            order.getCreatedAt()
        );
    }
}
