-- SmartOrder initial schema
-- Week 1: products + orders + order_lines

CREATE TABLE products (
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    price_amount NUMERIC(19, 4) NOT NULL,
    price_currency VARCHAR(3) NOT NULL DEFAULT 'EUR',
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    active      BOOLEAN NOT NULL DEFAULT true,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE orders (
    id              UUID PRIMARY KEY,
    customer_id     UUID NOT NULL,
    status          VARCHAR(50) NOT NULL,
    total_amount    NUMERIC(19, 4),
    total_currency  VARCHAR(3) NOT NULL DEFAULT 'EUR',
    placed_at       TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE order_lines (
    id          UUID PRIMARY KEY,
    order_id    UUID NOT NULL REFERENCES orders(id),
    product_id  UUID NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity    INTEGER NOT NULL,
    unit_price_amount   NUMERIC(19, 4) NOT NULL,
    unit_price_currency VARCHAR(3) NOT NULL DEFAULT 'EUR'
);

CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_lines_order_id ON order_lines(order_id);
