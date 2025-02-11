INSERT INTO orders (order_number, total_amount, order_date, order_consumer, delivery_address, payment_method, delivery_method)
VALUES ('ORDER-001',
         1000.00,
        CURRENT_TIMESTAMP,
        'Иванов Иван',
        'ул. Ленина, 1',
        'card',
        'door_delivery');

INSERT INTO orderdetails (product_article, product_name, product_quantity, product_price, order_id)
VALUES (
        1,
        'product',
        1,
        1000.00,
        1
       );
INSERT INTO orderdetails (product_article, product_name, product_quantity, product_price, order_id)
VALUES (
           2,
           'product2',
           1,
           2000.00,
           1
       );