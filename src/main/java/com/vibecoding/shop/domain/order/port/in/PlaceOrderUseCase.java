package com.vibecoding.shop.domain.order.port.in;

import com.vibecoding.shop.domain.order.dto.PlaceOrderCommand;
import com.vibecoding.shop.domain.order.dto.OrderResponse;

/** PART 2.2 — 헥사고날: Inbound Port */
public interface PlaceOrderUseCase {
    OrderResponse placeOrder(PlaceOrderCommand command);
}
