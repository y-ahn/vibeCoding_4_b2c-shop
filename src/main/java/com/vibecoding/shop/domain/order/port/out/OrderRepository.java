package com.vibecoding.shop.domain.order.port.out;

import com.vibecoding.shop.domain.order.entity.Order;
import com.vibecoding.shop.domain.order.dto.OrderSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

/** PART 2.2 — 헥사고날: Outbound Port (Repository 인터페이스) */
public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    Page<OrderSummaryDto> findByMemberId(Long memberId, Pageable pageable);
}
