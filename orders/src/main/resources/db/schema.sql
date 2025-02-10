CREATE TABLE IF NOT EXISTS orders
(
    ID              BIGSERIAL PRIMARY KEY,
    OrderNumber     VARCHAR(50) UNIQUE NOT NULL,
    TotalAmount     DECIMAL(15, 2)     NOT NULL,
    OrderDate       TIMESTAMP          NOT NULL,
    Recipient       VARCHAR(255)       NOT NULL,
    DeliveryAddress VARCHAR(255)       NOT NULL,
    PaymentType     VARCHAR(20)        NOT NULL,
    DeliveryType    VARCHAR(30)        NOT NULL
);

CREATE TABLE IF NOT EXISTS orderdetails
(
    ID             BIGSERIAL PRIMARY KEY,
    ProductArticle VARCHAR(50),
    ProductName    VARCHAR(255),
    Quantity       INTEGER,
    UnitPrice      DECIMAL(15, 2),
    OrderID        BIGINT,
    FOREIGN KEY (OrderID) REFERENCES Orders (ID)
);