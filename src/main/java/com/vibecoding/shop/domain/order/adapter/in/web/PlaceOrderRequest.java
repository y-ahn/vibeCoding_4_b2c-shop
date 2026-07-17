package com.vibecoding.shop.domain.order.adapter.in.web;

import com.vibecoding.shop.domain.order.dto.PlaceOrderCommand;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter @NoArgsConstructor
public class PlaceOrderRequest {
    @NotEmpty private List<PlaceOrderCommand.OrderItemCommand> items;
    @NotBlank private String receiverName;
    @NotBlank private String receiverPhone;
    @NotBlank private String city;
    @NotBlank private String street;
    @NotBlank private String zipCode;
}
