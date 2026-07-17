-- ============================================================
-- 바이브코딩 Vol.4 — B2C 이커머스 플랫폼
-- V1: 초기 스키마 생성 (DDD + 헥사고날 아키텍처 적용)
-- ============================================================

-- 회원
CREATE TABLE members (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(100) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    name         VARCHAR(50)  NOT NULL,
    phone        VARCHAR(20),
    role         ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
    point        INT          NOT NULL DEFAULT 0,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 주소 (값 객체 — members 테이블에 임베드)
ALTER TABLE members
    ADD COLUMN city       VARCHAR(100),
    ADD COLUMN street     VARCHAR(200),
    ADD COLUMN zip_code   VARCHAR(20);

-- 상품 카테고리
CREATE TABLE categories (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    parent_id  BIGINT,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- 상품 (Soft Delete 적용)
CREATE TABLE products (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id  BIGINT,
    name         VARCHAR(200) NOT NULL,
    description  TEXT,
    price        INT          NOT NULL,
    currency     VARCHAR(10)  NOT NULL DEFAULT 'KRW',
    stock        INT          NOT NULL DEFAULT 0,
    product_type ENUM('NORMAL','DIGITAL','SUBSCRIPTION') NOT NULL DEFAULT 'NORMAL',
    deleted_at   DATETIME,                              -- Soft Delete
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 주문
CREATE TABLE orders (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id       BIGINT       NOT NULL,
    status          ENUM('PAYMENT_WAITING','PREPARING','SHIPPED','DELIVERED','CANCELLED') NOT NULL,
    total_amount    INT          NOT NULL,
    currency        VARCHAR(10)  NOT NULL DEFAULT 'KRW',
    receiver_name   VARCHAR(50),
    receiver_phone  VARCHAR(20),
    city            VARCHAR(100),
    street          VARCHAR(200),
    zip_code        VARCHAR(20),
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- 주문 상품 (OrderLine)
CREATE TABLE order_lines (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    product_name VARCHAR(200) NOT NULL,  -- 주문 시점 스냅샷
    price       INT    NOT NULL,          -- 주문 시점 가격
    currency    VARCHAR(10) NOT NULL DEFAULT 'KRW',
    quantity    INT    NOT NULL,
    FOREIGN KEY (order_id)   REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 결제
CREATE TABLE payments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id        BIGINT       NOT NULL,
    member_id       BIGINT       NOT NULL,
    amount          INT          NOT NULL,
    payment_method  ENUM('CARD','KAKAO_PAY','NAVER_PAY','TOSS') NOT NULL,
    status          ENUM('PENDING','COMPLETED','FAILED','REFUNDED') NOT NULL,
    pg_transaction_id VARCHAR(100),     -- PG사 거래 ID
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id)  REFERENCES orders(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Outbox 이벤트 (이벤트 유실 방지)
CREATE TABLE outbox_events (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    aggregate_type  VARCHAR(50)  NOT NULL,  -- "ORDER", "PAYMENT"
    aggregate_id    VARCHAR(50)  NOT NULL,
    event_type      VARCHAR(100) NOT NULL,  -- "ORDER_PAID", "ORDER_CANCELLED"
    payload         TEXT         NOT NULL,  -- JSON
    published       BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 알림 이력
CREATE TABLE notifications (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id    BIGINT       NOT NULL,
    type         ENUM('EMAIL','PUSH','SMS') NOT NULL,
    title        VARCHAR(200) NOT NULL,
    content      TEXT         NOT NULL,
    sent_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- ── 인덱스 (PART 3 성능 최적화) ──────────────────────────────────
CREATE INDEX idx_products_category    ON products(category_id);
CREATE INDEX idx_products_deleted_at  ON products(deleted_at);
CREATE INDEX idx_products_created_at  ON products(created_at DESC);
CREATE INDEX idx_orders_member_id     ON orders(member_id);
CREATE INDEX idx_orders_status        ON orders(status);
CREATE INDEX idx_orders_created_at    ON orders(created_at DESC);
CREATE INDEX idx_order_lines_order    ON order_lines(order_id);
CREATE INDEX idx_payments_order_id    ON payments(order_id);
CREATE INDEX idx_outbox_published     ON outbox_events(published, created_at);
CREATE INDEX idx_notifications_member ON notifications(member_id);

-- ── 기본 데이터 ──────────────────────────────────────────────────
INSERT INTO categories (name) VALUES ('전자제품'), ('의류'), ('식품'), ('도서');
INSERT INTO categories (name, parent_id) VALUES
    ('스마트폰', 1), ('노트북', 1), ('태블릿', 1),
    ('남성의류', 2), ('여성의류', 2);
