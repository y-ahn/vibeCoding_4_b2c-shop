package com.vibecoding.shop.domain.product.adapter.out.persistence;

import com.vibecoding.shop.domain.order.port.out.ProductStockPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** 헥사고날: ProductStockPort Outbound Adapter 구현체 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductStockAdapter implements ProductStockPort {

    private final ProductJpaRepository productRepo;

    @Override
    @Transactional
    public boolean checkAndDecreaseStock(Long productId, int quantity) {
        var product = productRepo.findByIdWithLock(productId).orElse(null);
        if (product == null || !product.hasStock(quantity)) return false;
        product.decreaseStock(quantity);
        return true;
    }

    @Override
    @Transactional
    public void restoreStock(Long productId, int quantity) {
        productRepo.findById(productId).ifPresent(p -> p.restoreStock(quantity));
        log.info("재고 복구 완료. productId={}, quantity={}", productId, quantity);
    }
}
