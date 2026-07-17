package com.vibecoding.shop.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "order_lines")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;  // 주문 시점 스냅샷

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="amount",   column=@Column(name="price")),
        @AttributeOverride(name="currency", column=@Column(name="currency"))
    })
    private Money price;  // 주문 시점 가격 스냅샷

    @Column(nullable = false)
    private int quantity;

    public static OrderLine of(Long productId, String productName, Money price, int quantity) {
        OrderLine line = new OrderLine();
        line.productId   = productId;
        line.productName = productName;
        line.price       = price;
        line.quantity    = quantity;
        return line;
    }

    public Money getSubtotal() { return price.multiply(quantity); }
}
