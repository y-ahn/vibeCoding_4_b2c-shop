package com.vibecoding.shop.domain.payment.application;

import com.vibecoding.shop.domain.payment.entity.Payment;
import com.vibecoding.shop.domain.payment.adapter.out.persistence.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

/**
 * PART 4.3 — 전략 패턴: 결제 수단별 처리
 * Map<String, PaymentStrategy> → Spring이 Bean 이름으로 자동 주입
 */
@Service @Transactional @RequiredArgsConstructor @Slf4j
public class PaymentService {

    private final Map<String, PaymentStrategy> strategies; // CARD, KAKAO_PAY 등 자동 주입
    private final PaymentJpaRepository paymentRepo;

    public Long pay(Long orderId, Long memberId, int amount, String paymentMethod) {
        PaymentStrategy strategy = strategies.get(paymentMethod);
        if (strategy == null)
            throw new IllegalArgumentException("지원하지 않는 결제 수단: " + paymentMethod);

        Payment.PaymentMethod method = Payment.PaymentMethod.valueOf(paymentMethod);
        Payment payment = Payment.create(orderId, memberId, amount, method);

        try {
            String pgTxId = strategy.process(orderId, amount); // PG사 연동
            payment.complete(pgTxId);
            log.info("결제 완료. orderId={}, method={}, pgTxId={}", orderId, paymentMethod, pgTxId);
        } catch (Exception e) {
            payment.fail();
            log.error("결제 실패. orderId={}, reason={}", orderId, e.getMessage());
            throw new RuntimeException("결제 처리 실패: " + e.getMessage());
        }

        return paymentRepo.save(payment).getId();
    }

    public void refund(Long paymentId) {
        paymentRepo.findById(paymentId).ifPresent(p -> {
            p.refund();
            log.info("환불 완료. paymentId={}", paymentId);
        });
    }
}
