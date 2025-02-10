INSERT INTO orders (OrderNumber, TotalAmount, OrderDate, Recipient, DeliveryAddress, PaymentType, DeliveryType)
VALUES ('ORDER-001',
         1000.00,
        CURRENT_TIMESTAMP,
        'Иванов Иван',
        'ул. Ленина, 1',
        'card',
        'door_delivery');

INSERT INTO orderdetails (productarticle, productname, quantity, unitprice, orderid)
VALUES (
        1,
        'product',
        1,
        1000.00,
        1
       );
INSERT INTO orderdetails (productarticle, productname, quantity, unitprice, orderid)
VALUES (
           2,
           'product2',
           1,
           2000.00,
           1
       );