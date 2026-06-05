CREATE TABLE couriers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE INDEX idx_couriers_email ON couriers(email);

CREATE INDEX idx_couriers_status ON couriers(status);