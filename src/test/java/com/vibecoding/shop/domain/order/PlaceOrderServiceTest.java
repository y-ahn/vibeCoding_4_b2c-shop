package com.vibecoding.shop.domain.order;

import com.vibecoding.shop.domain.order.application.PlaceOrderService;
import com.vibecoding.shop.domain.order.dto.PlaceOrderCommand;
import com.vibecoding.shop.domain.order.dto.OrderResponse;
import com.vibecoding.shop.domain.order.entity.*;
import com.vibecoding.shop.domain.order.port.out.*;
import com.vibecoding.shop.domain.product.port.out.ProductQueryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

/** PART 6 — 단위 테스트: 헥사고날 아키텍처에서의 테스트 전략 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PlaceOrderService 단위 테스트")
class PlaceOrderServiceTest {

    @Mock OrderRepository    orderRepository;
    @Mock ProductStockPort   productStockPort;
    @Mock EventPublisherPort eventPublisher;
    @Mock ProductQueryPort   productQueryPort;

    @InjectMocks PlaceOrderService placeOrderService;

    @Test
    @DisplayName("주문 생성 성공 — 정상 재고 상품")
    void placeOrder_success() {
        // given
        var mockProduct = new MockProduct(1L, "테스트 상품", 10000);
        given(productStockPort.checkAndDecreaseStock(1L, 2)).willReturn(true);
        given(productQueryPort.findById(1L)).willReturn(Optional.of(mockProduct));
        given(orderRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        var command = new PlaceOrderCommand(
                1L,
                List.of(new PlaceOrderCommand.OrderItemCommand(1L, 2)),
                "홍길동", "010-1234-5678", "서울시", "강남대로 1", "06000");

        // when
        OrderResponse response = placeOrderService.placeOrder(command);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMemberId()).isEqualTo(1L);
        verify(orderRepository, times(1)).save(any());
        verify(eventPublisher, times(1)).publish(eq("ORDER"), any(), eq("ORDER_CREATED"), any());
    }

    @Test
    @DisplayName("주문 실패 — 재고 부족")
    void placeOrder_insufficientStock() {
        // given
        given(productStockPort.checkAndDecreaseStock(1L, 100)).willReturn(false);

        var command = new PlaceOrderCommand(
                1L,
                List.of(new PlaceOrderCommand.OrderItemCommand(1L, 100)),
                "홍길동", "010-1234-5678", "서울시", "강남대로 1", "06000");

        // when & then
        assertThatThrownBy(() -> placeOrderService.placeOrder(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("재고가 부족");
    }

    // 테스트용 Product Mock
    static class MockProduct extends com.vibecoding.shop.domain.product.entity.Product {
        MockProduct(Long id, String name, int price) {
            // 필드 세팅 (테스트용)
        }
        @Override public String getName() { return "테스트 상품"; }
        @Override public int getPrice()   { return 10000; }
    }
}
