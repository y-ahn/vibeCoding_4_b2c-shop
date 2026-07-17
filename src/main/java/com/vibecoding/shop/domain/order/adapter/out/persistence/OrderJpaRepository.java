package com.vibecoding.shop.domain.order.adapter.out.persistence;

import com.vibecoding.shop.domain.order.entity.Order;
import com.vibecoding.shop.domain.order.dto.OrderSummaryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vibecoding.shop.domain.order.port.out.OrderRepository;
import java.util.Optional;
import java.util.List;

// Spring Data JPA
interface OrderSpringDataRepository extends JpaRepository<Order, Long> {}

/** PART 2.2 — 헥사고날 Outbound Adapter: JPA + QueryDSL (CQRS) */
@Repository
@RequiredArgsConstructor
public class OrderJpaRepository implements OrderRepository {

    private final OrderSpringDataRepository springDataRepo;
    private final JPAQueryFactory queryFactory;

    @Override
    public Order save(Order order) { return springDataRepo.save(order); }

    @Override
    public Optional<Order> findById(Long id) { return springDataRepo.findById(id); }

    /** PART 3.6 — CQRS: 읽기 전용 QueryDSL DTO 조회 */
    @Override
    public Page<OrderSummaryDto> findByMemberId(Long memberId, Pageable pageable) {
        var o = com.vibecoding.shop.domain.order.entity.QOrder.order;

        List<OrderSummaryDto> content = queryFactory
            .select(Projections.constructor(OrderSummaryDto.class,
                o.id, o.status, o.totalAmount.amount, o.createdAt))
            .from(o)
            .where(o.memberId.eq(memberId))
            .orderBy(o.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory.select(o.count()).from(o)
                .where(o.memberId.eq(memberId)).fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
