-- Create database and user (if not exists)
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories(id),
    created_at BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS carts (
    id SERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL UNIQUE REFERENCES customers(id),
    created_at BIGINT NOT NULL,
    updated_at BIGINT
);

CREATE TABLE IF NOT EXISTS cart_items (
    id SERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL REFERENCES carts(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    product_name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    created_at BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    created_at BIGINT NOT NULL
);

-- Sample data
INSERT INTO categories (name, description, created_at) VALUES 
    ('Electronics', 'Electronic devices and gadgets', EXTRACT(EPOCH FROM NOW()) * 1000),
    ('Books', 'All kinds of books', EXTRACT(EPOCH FROM NOW()) * 1000),
    ('Clothing', 'Apparel and fashion items', EXTRACT(EPOCH FROM NOW()) * 1000)
ON CONFLICT (name) DO NOTHING;

INSERT INTO products (name, description, price, quantity, category_id, created_at) VALUES 
    ('Laptop', 'High performance laptop', 999.99, 50, 1, EXTRACT(EPOCH FROM NOW()) * 1000),
    ('Wireless Mouse', 'Ergonomic wireless mouse', 29.99, 100, 1, EXTRACT(EPOCH FROM NOW()) * 1000),
    ('Java Programming', 'Learn Java from scratch', 39.99, 30, 2, EXTRACT(EPOCH FROM NOW()) * 1000),
    ('T-Shirt', 'Comfortable cotton t-shirt', 19.99, 200, 3, EXTRACT(EPOCH FROM NOW()) * 1000),
    ('Jeans', 'Classic blue denim jeans', 59.99, 75, 3, EXTRACT(EPOCH FROM NOW()) * 1000),
    ('USB-C Cable', 'Fast charging USB-C cable', 12.99, 150, 1, EXTRACT(EPOCH FROM NOW()) * 1000)
ON CONFLICT DO NOTHING;
