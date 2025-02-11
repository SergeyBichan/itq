INSERT INTO orders (order_number, total_amount, order_date, order_consumer, delivery_address, payment_method,
                    delivery_method)
VALUES ('ORDER-001',
        0,
        CURRENT_TIMESTAMP,
        'Иванов Иван',
        'ул. Ленина, 1',
        'card',
        'door_delivery');

INSERT INTO orderdetails (product_article, product_name, product_quantity, product_price, order_id)
VALUES (1,
        'product',
        2,
        1000.00,
        1);
INSERT INTO orderdetails (product_article, product_name, product_quantity, product_price, order_id)
VALUES (2,
        'product2',
        1,
        2000.00,
        1);
UPDATE orders o
SET total_amount = (SELECT SUM(od.product_price * od.product_quantity)
                    FROM orderdetails od
                    WHERE o.id = od.order_id)
WHERE o.id = 1;