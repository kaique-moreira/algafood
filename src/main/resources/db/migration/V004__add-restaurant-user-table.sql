CREATE TABLE restaurant_user (
	restaurant_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	
	PRIMARY KEY (restaurant_id, user_id)
);


ALTER TABLE restaurant_user ADD constraint fk_restaurant_user_user
FOREIGN KEY (user_id) references tb_user (id);

ALTER TABLE restaurant_user ADD constraint fk_restaurant_user_restaurant
FOREIGN KEY (restaurant_id) references tb_restaurant (id);


