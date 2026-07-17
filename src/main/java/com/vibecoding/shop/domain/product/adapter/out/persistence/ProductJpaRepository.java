package com.vibecoding.shop.domain.product.adapter.out.persistence;

import com.vibecoding.shop.domain.product.dto.ProductSearchCondition;
import com.vibecoding.shop.domain.product.dto.ProductSummaryDto;
import com.vibecoding.shop.domain.product.entity.Product;
import com.vibecoding.shop.domain.product.entity.QProduct;
import com.vibecoding.shop.domain.product.port.out.ProductQueryPort;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

interface ProductSpringDataRepo extends JpaRepository<Product, Long> {
    // 비관적 락 — 재고 차감 시 사용 (PART 3.6)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);
}

/**
 * PART 3.6 — CQRS: QueryDSL DTO 직접 조회 (읽기 최적화)
 * PART 4.6 — Specification 패턴: 동적 검색 조건
 */
@Repository
@RequiredArgsConstructor
public class ProductJpaRepository implements ProductQueryPort {

    private final ProductSpringDataRepo springDataRepo;
    private final JPAQueryFactory       queryFactory;

    @Override
    public Optional<Product> findById(Long id) {
        return springDataRepo.findById(id);
    }

    public Optional<Product> findByIdWithLock(Long id) {
        return springDataRepo.findByIdWithLock(id);
    }

    /** CQRS 읽기 모델 — 필요한 필드만 DTO로 직접 조회 */
    public Page<ProductSummaryDto> search(ProductSearchCondition cond, Pageable pageable) {
        QProduct p = QProduct.product;

        List<ProductSummaryDto> content = queryFactory
            .select(Projections.constructor(ProductSummaryDto.class,
                p.id, p.name, p.price, p.stock))
            .from(p)
            .where(
                keywordContains(cond.getKeyword()),
                priceGoe(cond.getMinPrice()),
                priceLoe(cond.getMaxPrice())
            )
            .orderBy(p.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory.select(p.count()).from(p)
            .where(keywordContains(cond.getKeyword())).fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    // 동적 조건 (null이면 조건 제외)
    private BooleanExpression keywordContains(String kw) {
        QProduct p = QProduct.product;
        return kw != null ? p.name.contains(kw) : null;
    }
    private BooleanExpression priceGoe(Integer min) {
        QProduct p = QProduct.product;
        return min != null ? p.price.goe(min) : null;
    }
    private BooleanExpression priceLoe(Integer max) {
        QProduct p = QProduct.product;
        return max != null ? p.price.loe(max) : null;
    }
}
