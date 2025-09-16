CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT order_items_orders_id_fk FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT order_items_products_id_fk FOREIGN KEY (product_id) REFERENCES products (id)
);