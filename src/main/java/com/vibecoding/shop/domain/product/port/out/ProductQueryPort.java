package com.vibecoding.shop.domain.product.port.out;

import com.vibecoding.shop.domain.product.entity.Product;
import java.util.Optional;

public interface ProductQueryPort {
    Optional<Product> findById(Long id);
}
