package com.vibecoding.shop.domain.order.port.in;

import com.vibecoding.shop.domain.order.dto.PayOrderCommand;

public interface PayOrderUseCase {
    void payOrder(PayOrderCommand command);
}
