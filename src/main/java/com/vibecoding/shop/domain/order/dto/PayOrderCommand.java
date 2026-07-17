package com.vibecoding.shop.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class PayOrderCommand {
    private final Long orderId;
    private final Long memberId;
    private final String paymentMethod;
}
