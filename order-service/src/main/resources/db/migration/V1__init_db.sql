CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    sender_lat DOUBLE PRECISION NOT NULL,
    sender_lng DOUBLE PRECISION NOT NULL,
    delivery_lat DOUBLE PRECISION NOT NULL,
    delivery_lng DOUBLE PRECISION NOT NULL,
    comment VARCHAR(500),
    weight DOUBLE PRECISION NOT NULL,
    volume DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL,
    courier_id BIGINT
);

CREATE INDEX idx_orders_courier_id ON orders(courier_id);