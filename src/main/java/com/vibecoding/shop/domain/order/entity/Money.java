package com.vibecoding.shop.domain.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PART 1.2 — 값 객체(Value Object)
 * - 불변(Immutable): 연산 결과로 새 객체 반환
 * - @Embeddable: Order, OrderLine, Product 테이블에 인라인 저장
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {
    @Column(name = "amount")   private int amount;
    @Column(name = "currency") private String currency;

    public static final Money ZERO = new Money(0, "KRW");

    public Money(int amount, String currency) {
        if (amount < 0) throw new IllegalArgumentException("금액은 0 이상이어야 합니다.");
        this.amount = amount;
        this.currency = currency;
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount + other.amount, this.currency);
    }
    public Money multiply(int factor) {
        return new Money(this.amount * factor, this.currency);
    }
    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount > other.amount;
    }
    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency))
            throw new IllegalArgumentException("통화가 다릅니다: " + this.currency + " vs " + other.currency);
    }
}
