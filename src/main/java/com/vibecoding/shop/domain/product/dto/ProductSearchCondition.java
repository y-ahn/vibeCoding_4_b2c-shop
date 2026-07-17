package com.vibecoding.shop.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ProductSearchCondition {
    private String keyword;
    private Long   categoryId;
    private Integer minPrice;
    private Integer maxPrice;
}
