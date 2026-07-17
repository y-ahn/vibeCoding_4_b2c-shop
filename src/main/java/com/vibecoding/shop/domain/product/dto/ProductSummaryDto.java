package com.vibecoding.shop.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class ProductSummaryDto {
    private Long   id;
    private String name;
    private int    price;
    private int    stock;
    private String categoryName;
}
