-- add foreign key to product view table
ALTER TABLE product_view_tbl
ADD FOREIGN KEY (product_id) REFERENCES product_tbl(id),
ADD	FOREIGN KEY (user_id) REFERENCES user_tbl(id);