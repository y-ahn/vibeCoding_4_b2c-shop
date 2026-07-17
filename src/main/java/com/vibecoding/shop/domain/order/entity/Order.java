package com.vibecoding.shop.domain.order.entity;

import com.vibecoding.shop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;
import java.util.ArrayList;
import java.util.List;

/**
 * PART 8.2 — Order Aggregate Root (DDD 적용)
 * - AbstractAggregateRoot: 도메인 이벤트 자동 발행
 * - 비즈니스 메서드: pay(), cancel(), ship(), deliver()
 * - 외부 참조: memberId (ID 참조 패턴)
 */
@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AbstractAggregateRoot<Order> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;  // Member Aggregate는 ID로만 참조

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLine> orderLines = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="amount",   column=@Column(name="total_amount")),
        @AttributeOverride(name="currency", column=@Column(name="currency"))
    })
    private Money totalAmount;

    @Embedded private ShippingInfo shippingInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    // ── 정적 팩토리 ────────────────────────────────────────────────
    public static Order create(Long memberId, List<OrderLine> lines,
                                ShippingInfo shippingInfo) {
        Order order = new Order();
        order.memberId      = memberId;
        order.shippingInfo  = shippingInfo;
        order.status        = OrderStatus.PAYMENT_WAITING;
        lines.forEach(order::addOrderLine);
        order.totalAmount   = order.calculateTotal();
        return order;
    }

    private void addOrderLine(OrderLine line) { orderLines.add(line); }

    // ── 비즈니스 메서드 (상태 패턴 — PART 4.5) ──────────────────────
    public void pay() {
        if (status != OrderStatus.PAYMENT_WAITING)
            throw new IllegalStateException("결제 대기 상태만 결제 가능합니다.");
        this.status = OrderStatus.PREPARING;
        registerEvent(new OrderPaidEvent(this.id, this.memberId, this.totalAmount));
    }

    public void cancel() {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED)
            throw new IllegalStateException("배송 중/완료 주문은 취소할 수 없습니다.");
        this.status = OrderStatus.CANCELLED;
        registerEvent(new OrderCancelledEvent(this.id, this.memberId));
    }

    public void ship() {
        if (status != OrderStatus.PREPARING)
            throw new IllegalStateException("상품 준비 중 상태만 배송 처리 가능합니다.");
        this.status = OrderStatus.SHIPPED;
    }

    public void deliver() {
        if (status != OrderStatus.SHIPPED)
            throw new IllegalStateException("배송 중 상태만 배송 완료 처리 가능합니다.");
        this.status = OrderStatus.DELIVERED;
    }

    private Money calculateTotal() {
        return orderLines.stream()
                .map(OrderLine::getSubtotal)
                .reduce(Money.ZERO, Money::add);
    }
}
