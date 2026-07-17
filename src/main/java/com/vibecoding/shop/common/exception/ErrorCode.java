package com.vibecoding.shop.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 회원
    MEMBER_NOT_FOUND      ("해당 회원을 찾을 수 없습니다.",           HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL       ("이미 사용 중인 이메일입니다.",             HttpStatus.CONFLICT),
    INVALID_PASSWORD      ("비밀번호가 올바르지 않습니다.",            HttpStatus.UNAUTHORIZED),
    // 상품
    PRODUCT_NOT_FOUND     ("해당 상품을 찾을 수 없습니다.",            HttpStatus.NOT_FOUND),
    INSUFFICIENT_STOCK    ("재고가 부족합니다.",                        HttpStatus.BAD_REQUEST),
    // 주문
    ORDER_NOT_FOUND       ("해당 주문을 찾을 수 없습니다.",            HttpStatus.NOT_FOUND),
    ORDER_STATUS_INVALID  ("현재 주문 상태에서는 해당 작업이 불가합니다.", HttpStatus.BAD_REQUEST),
    ORDER_MEMBER_MISMATCH ("본인의 주문만 조회할 수 있습니다.",        HttpStatus.FORBIDDEN),
    // 결제
    PAYMENT_NOT_FOUND     ("결제 정보를 찾을 수 없습니다.",            HttpStatus.NOT_FOUND),
    PAYMENT_FAILED        ("결제 처리에 실패했습니다.",                 HttpStatus.BAD_REQUEST),
    UNSUPPORTED_PAYMENT   ("지원하지 않는 결제 수단입니다.",            HttpStatus.BAD_REQUEST),
    // 인증
    UNAUTHORIZED          ("인증이 필요합니다.",                        HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED         ("접근 권한이 없습니다.",                     HttpStatus.FORBIDDEN),
    INVALID_TOKEN         ("유효하지 않은 토큰입니다.",                 HttpStatus.UNAUTHORIZED),
    // 서버
    INTERNAL_SERVER_ERROR ("서버 오류가 발생했습니다.",                 HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;
}
