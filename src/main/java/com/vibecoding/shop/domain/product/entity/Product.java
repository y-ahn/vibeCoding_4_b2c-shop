package com.vibecoding.shop.domain.product.entity;

import com.vibecoding.shop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * PART 3.1 — 상속 매핑: SINGLE_TABLE 전략
 * PART 3.3 — Soft Delete: @SQLDelete + @Where
 * PART 3.4 — Audit 자동화: BaseEntity 상속
 */
@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.SQLDelete(sql = "UPDATE products SET deleted_at = NOW() WHERE id = ?")
@org.hibernate.annotations.Where(clause = "deleted_at IS NULL")
public class Product extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)  private String name;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false)  private int price;
    @Column(nullable = false)  private int stock;
    @Column(name = "deleted_at") private java.time.LocalDateTime deletedAt;

    public static Product create(String name, String description, int price, int stock) {
        Product p = new Product();
        p.name = name; p.description = description;
        p.price = price; p.stock = stock;
        return p;
    }

    public boolean hasStock(int quantity) { return this.stock >= quantity; }

    public void decreaseStock(int quantity) {
        if (!hasStock(quantity)) throw new IllegalStateException("재고 부족");
        this.stock -= quantity;
    }

    public void restoreStock(int quantity) { this.stock += quantity; }
    public void changePrice(int newPrice)  { this.price = newPrice; }
    public String getName() { return name; }
    public int getPrice()   { return price; }
}
