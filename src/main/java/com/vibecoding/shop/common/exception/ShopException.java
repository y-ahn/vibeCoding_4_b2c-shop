package com.vibecoding.shop.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 최상위 커스텀 예외
@Getter
public abstract class ShopException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    protected ShopException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode  = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }
}

class BusinessException extends ShopException {
    public BusinessException(ErrorCode errorCode) { super(errorCode); }
}

// 도메인별 예외
class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() { super(ErrorCode.MEMBER_NOT_FOUND); }
}
class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException() { super(ErrorCode.DUPLICATE_EMAIL); }
}
class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException() { super(ErrorCode.PRODUCT_NOT_FOUND); }
}
class InsufficientStockException extends BusinessException {
    public InsufficientStockException() { super(ErrorCode.INSUFFICIENT_STOCK); }
}
class OrderNotFoundException extends BusinessException {
    public OrderNotFoundException() { super(ErrorCode.ORDER_NOT_FOUND); }
}
class OrderStatusException extends BusinessException {
    public OrderStatusException() { super(ErrorCode.ORDER_STATUS_INVALID); }
    public OrderStatusException(String msg) { super(ErrorCode.ORDER_STATUS_INVALID); }
}
class PaymentFailedException extends BusinessException {
    public PaymentFailedException() { super(ErrorCode.PAYMENT_FAILED); }
}
