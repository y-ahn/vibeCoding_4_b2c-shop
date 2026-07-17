package com.vibecoding.shop.domain.order.port.out;

/** 상품 서비스 호출 Outbound Port (Feign 또는 내부 호출) */
public interface ProductStockPort {
    boolean checkAndDecreaseStock(Long productId, int quantity);
    void restoreStock(Long productId, int quantity);
}
