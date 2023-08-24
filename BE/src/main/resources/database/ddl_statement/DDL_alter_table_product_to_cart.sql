-- add foreign key to product view table
ALTER TABLE product_to_cart_tbl
ADD FOREIGN KEY (product_id) REFERENCES product_tbl(id)