package com.vibecoding.shop.domain.order.application;

import com.vibecoding.shop.domain.order.dto.*;
import com.vibecoding.shop.domain.order.entity.*;
import com.vibecoding.shop.domain.order.port.in.PlaceOrderUseCase;
import com.vibecoding.shop.domain.order.port.out.*;
import com.vibecoding.shop.domain.product.port.out.ProductQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PART 8.3 — 헥사고날 아키텍처: Use Case 구현
 * - 도메인 객체(Order, OrderLine)만 의존
 * - 인프라(JPA, Kafka)는 Port를 통해 간접 의존
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepository   orderRepository;
    private final ProductStockPort  productStockPort;
    private final EventPublisherPort eventPublisher;
    private final ProductQueryPort  productQueryPort;

    @Override
    public OrderResponse placeOrder(PlaceOrderCommand command) {
        // 1. 재고 확인 및 차감
        List<OrderLine> lines = command.getItems().stream()
            .map(item -> {
                boolean ok = productStockPort.checkAndDecreaseStock(
                        item.getProductId(), item.getQuantity());
                if (!ok) throw new IllegalStateException(
                        "상품 재고가 부족합니다. productId=" + item.getProductId());

                var product = productQueryPort.findById(item.getProductId())
                        .orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다."));

                return OrderLine.of(
                        item.getProductId(),
                        product.getName(),
                        new Money(product.getPrice(), "KRW"),
                        item.getQuantity());
            })
            .collect(Collectors.toList());

        // 2. 주문 생성 (Aggregate)
        ShippingInfo shippingInfo = new ShippingInfo(
                command.getReceiverName(), command.getReceiverPhone(),
                command.getCity(), command.getStreet(), command.getZipCode());

        Order order = Order.create(command.getMemberId(), lines, shippingInfo);
        Order saved = orderRepository.save(order);

        // 3. Outbox 이벤트 등록 (PART 5.4)
        eventPublisher.publish("ORDER", saved.getId().toString(),
                "ORDER_CREATED", new OrderCreatedPayload(saved.getId(), command.getMemberId()));

        log.info("주문 생성 완료. orderId={}, memberId={}", saved.getId(), command.getMemberId());
        return OrderResponse.from(saved);
    }

    record OrderCreatedPayload(Long orderId, Long memberId) {}
}
