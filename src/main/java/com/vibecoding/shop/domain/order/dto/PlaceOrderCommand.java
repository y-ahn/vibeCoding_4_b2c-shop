package com.vibecoding.shop.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/** PART 4.2 — 커맨드 패턴: 주문 생성 입력 */
@Getter @AllArgsConstructor
public class PlaceOrderCommand {
    private final Long memberId;
    private final List<OrderItemCommand> items;
    private final String receiverName;
    private final String receiverPhone;
    private final String city;
    private final String street;
    private final String zipCode;

    @Getter @AllArgsConstructor
    public static class OrderItemCommand {
        private final Long productId;
        private final int quantity;
    }
}
