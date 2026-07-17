package com.vibecoding.shop.domain.order.adapter.in.web;

import com.vibecoding.shop.common.response.ApiResponse;
import com.vibecoding.shop.domain.order.dto.*;
import com.vibecoding.shop.domain.order.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** PART 2.2 — 헥사고날 Inbound Adapter: REST API */
@Tag(name = "Order", description = "주문 API")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final PayOrderUseCase payOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    @Operation(summary = "주문 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> placeOrder(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody PlaceOrderRequest request) {
        PlaceOrderCommand command = new PlaceOrderCommand(
                memberId, request.getItems(),
                request.getReceiverName(), request.getReceiverPhone(),
                request.getCity(), request.getStreet(), request.getZipCode());
        return ApiResponse.success("주문이 생성되었습니다.", placeOrderUseCase.placeOrder(command));
    }

    @Operation(summary = "주문 단건 조회")
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrder(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long orderId) {
        return ApiResponse.success(getOrderUseCase.getOrder(orderId, memberId));
    }

    @Operation(summary = "내 주문 목록 (CQRS 읽기 모델)")
    @GetMapping("/me")
    public ApiResponse<Page<OrderSummaryDto>> getMyOrders(
            @AuthenticationPrincipal Long memberId,
            Pageable pageable) {
        return ApiResponse.success(getOrderUseCase.getMyOrders(memberId, pageable));
    }

    @Operation(summary = "주문 취소")
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long orderId) {
        cancelOrderUseCase.cancelOrder(orderId, memberId);
    }
}
