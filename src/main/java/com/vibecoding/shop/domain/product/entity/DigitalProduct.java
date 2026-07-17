package com.vibecoding.shop.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @DiscriminatorValue("DIGITAL")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DigitalProduct extends Product {
    @Column(name = "download_url") private String downloadUrl;

    public static DigitalProduct create(String name, String desc,
                                         int price, String downloadUrl) {
        DigitalProduct p = new DigitalProduct();
        p.downloadUrl = downloadUrl;
        return p;
    }
}
