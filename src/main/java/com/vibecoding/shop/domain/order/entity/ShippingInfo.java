package com.vibecoding.shop.domain.order.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** PART 1.2 — 배송 정보 값 객체 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShippingInfo {
    private String receiverName;
    private String receiverPhone;
    private String city;
    private String street;
    private String zipCode;
}
