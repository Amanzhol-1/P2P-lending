CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    balance NUMERIC(19,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    roles VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, roles),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    balance NUMERIC(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(19,2) NOT NULL,
    available BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    price NUMERIC(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    description TEXT
    );



CREATE TABLE IF NOT EXISTS outbox_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    processed BOOLEAN NOT NULL
);


INSERT INTO users (username, password, first_name, last_name, balance) VALUES
('john_doe', '$2b$12$TOuco4WLnNN7NEDftb5tsOgCGWgLuVOUgVuo/jQjzCfpI.ulMmrJG', 'John', 'Doe', 1000.00),
('aman_m', '$2b$12$TOuco4WLnNN7NEDftb5tsOgCGWgLuVOUgVuo/jQjzCfpI.ulMmrJG', 'Admin', 'User', 5000.00);

INSERT INTO user_roles (user_id, roles) VALUES
((SELECT id FROM users WHERE username = 'john_doe'), 'ROLE_USER'),
((SELECT id FROM users WHERE username = 'aman_m'), 'ROLE_USER');

INSERT INTO accounts (balance, currency, status) VALUES
(1000.00, 'USD', 'ACTIVE'),
(5000.00, 'USD', 'ACTIVE');

INSERT INTO products (name, description, price, available) VALUES
('NFT Art 1', 'Уникальное цифровое произведение искусства #1', 250.00, TRUE),
('NFT Art 2', 'Уникальное цифровое произведение искусства #2', 300.00, TRUE),
('NFT Art 3', 'Уникальное цифровое произведение искусства #3', 150.00, FALSE);

INSERT INTO orders (account_id, product_id, price, status, timestamp) VALUES (
    (SELECT id FROM accounts ORDER BY id LIMIT 1),
    (SELECT id FROM products WHERE name = 'NFT Art 1'),
    250.00,
    'PENDING',
    CURRENT_TIMESTAMP
);

INSERT INTO transactions (account_id, type, amount, timestamp, description) VALUES (
    (SELECT id FROM accounts ORDER BY id LIMIT 1),
    'CREDIT',
    250.00,
    CURRENT_TIMESTAMP,
    'Пополнение баланса за заказ NFT Art 1'
),
(
    (SELECT id FROM accounts ORDER BY id LIMIT 1),
    'DEBIT',
    100.00,
    CURRENT_TIMESTAMP,
    'Оплата комиссии'
);

INSERT INTO outbox_events (aggregate_id, event_type, payload, created_at, processed) VALUES (
    1,
    'TEST_EVENT',
    '{"message": "Это тестовое событие из Outbox"}',
    CURRENT_TIMESTAMP,
    FALSE
);
