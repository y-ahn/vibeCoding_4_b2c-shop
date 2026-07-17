package com.vibecoding.shop.domain.product.port.in;

import com.vibecoding.shop.domain.product.dto.ProductSummaryDto;
import com.vibecoding.shop.domain.product.dto.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchProductUseCase {
    Page<ProductSummaryDto> searchProducts(ProductSearchCondition condition, Pageable pageable);
}
