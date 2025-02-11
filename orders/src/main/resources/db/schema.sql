CREATE TABLE IF NOT EXISTS orders
(
    id              BIGSERIAL PRIMARY KEY,
    order_number     VARCHAR(50) UNIQUE NOT NULL,
    total_amount     DECIMAL(15, 2)     NOT NULL,
    order_date       TIMESTAMP          NOT NULL,
    order_consumer       VARCHAR(255)       NOT NULL,
    delivery_address VARCHAR(255)       NOT NULL,
    payment_method     VARCHAR(20)        NOT NULL,
    delivery_method    VARCHAR(30)        NOT NULL
);

CREATE TABLE IF NOT EXISTS orderdetails
(
    id             BIGSERIAL PRIMARY KEY,
    product_article VARCHAR(50),
    product_name    VARCHAR(255),
    product_quantity       INTEGER,
    product_price      DECIMAL(15, 2),
    order_id        BIGINT,
    FOREIGN KEY (order_id) REFERENCES Orders (ID)
);