CREATE TABLE IF NOT EXISTS order_service.t_order (
    id SERIAL PRIMARY KEY,
    order_id VARCHAR(255) UNIQUE,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_service.t_order_line_item (
    id SERIAL PRIMARY KEY,
    order_id SERIAL REFERENCES order_service.t_order(id),
    sku_id VARCHAR(255),
    price NUMERIC,
    quantity INTEGER
);
