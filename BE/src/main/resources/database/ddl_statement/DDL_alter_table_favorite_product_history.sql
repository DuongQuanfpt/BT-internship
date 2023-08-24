ALTER TABLE product_favorite_history_tbl
ADD FOREIGN KEY (product_id) REFERENCES product_tbl(id),
ADD	FOREIGN KEY (user_id) REFERENCES user_tbl(id);
