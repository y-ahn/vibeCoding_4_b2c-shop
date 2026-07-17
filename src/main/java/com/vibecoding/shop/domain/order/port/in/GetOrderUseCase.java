package com.vibecoding.shop.domain.order.port.in;

import com.vibecoding.shop.domain.order.dto.OrderResponse;
import com.vibecoding.shop.domain.order.dto.OrderSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetOrderUseCase {
    OrderResponse getOrder(Long orderId, Long memberId);
    Page<OrderSummaryDto> getMyOrders(Long memberId, Pageable pageable);
}
