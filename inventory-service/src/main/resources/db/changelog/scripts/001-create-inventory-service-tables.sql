CREATE TABLE IF NOT EXISTS inventory_service.t_inventory (
    id SERIAL PRIMARY KEY,
    sku_code VARCHAR(255) UNIQUE,
    quantity INTEGER,
    created_at TIMESTAMP
);
