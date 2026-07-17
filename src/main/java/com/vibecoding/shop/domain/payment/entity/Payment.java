package com.vibecoding.shop.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "payments")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)  private Long orderId;
    @Column(name = "member_id", nullable = false) private Long memberId;
    @Column(nullable = false)                      private int  amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "pg_transaction_id") private String pgTransactionId;
    @Column(name = "created_at")        private LocalDateTime createdAt;

    public static Payment create(Long orderId, Long memberId,
                                  int amount, PaymentMethod method) {
        Payment p = new Payment();
        p.orderId       = orderId;
        p.memberId      = memberId;
        p.amount        = amount;
        p.paymentMethod = method;
        p.status        = PaymentStatus.PENDING;
        p.createdAt     = LocalDateTime.now();
        return p;
    }

    public void complete(String pgTransactionId) {
        this.status            = PaymentStatus.COMPLETED;
        this.pgTransactionId   = pgTransactionId;
    }
    public void fail()   { this.status = PaymentStatus.FAILED; }
    public void refund() { this.status = PaymentStatus.REFUNDED; }

    public enum PaymentMethod { CARD, KAKAO_PAY, NAVER_PAY, TOSS }
    public enum PaymentStatus  { PENDING, COMPLETED, FAILED, REFUNDED }
}
