package com.vibecoding.shop.domain.product.adapter.in.web;

import com.vibecoding.shop.common.response.ApiResponse;
import com.vibecoding.shop.domain.product.dto.ProductSearchCondition;
import com.vibecoding.shop.domain.product.dto.ProductSummaryDto;
import com.vibecoding.shop.domain.product.port.in.SearchProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product", description = "상품 API")
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final SearchProductUseCase searchProductUseCase;

    @Operation(summary = "상품 목록 검색 (CQRS 읽기 모델)")
    @GetMapping
    public ApiResponse<Page<ProductSummaryDto>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            Pageable pageable) {

        ProductSearchCondition cond = new ProductSearchCondition();
        cond.setKeyword(keyword);
        cond.setCategoryId(categoryId);
        cond.setMinPrice(minPrice);
        cond.setMaxPrice(maxPrice);

        return ApiResponse.success(searchProductUseCase.searchProducts(cond, pageable));
    }
}
