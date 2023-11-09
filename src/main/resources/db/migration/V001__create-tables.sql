CREATE TABLE tb_cuisine (
  id SERIAL,
  name VARCHAR(60) NOT NULL,

  PRIMARY KEY(id)
);

CREATE TABLE tb_city (
  id SERIAL,
  name VARCHAR(60) NOT NULL,

  state_id BIGINT NOT NULL,

  PRIMARY KEY(id)
);

CREATE TABLE tb_state (
  id SERIAL,
  name VARCHAR(60) NOT NULL,

  PRIMARY KEY(id)
);

CREATE TABLE tb_restaurant (
	id SERIAL,
	name VARCHAR(80) NOT NULL,
	shipping_fee DECIMAL(10,2) NOT NULL,
  register_date TIMESTAMP NOT NULL,
	update_date TIMESTAMP NOT NULL,
	
	addres_city_id BIGINT,
	addres_postal_code VARCHAR(9),
	addres_street VARCHAR(100),
	addres_number VARCHAR(20),
	addres_complement VARCHAR(60),
	addres_district VARCHAR(60),
	
	cuisine_id BIGINT NOT NULL,

	PRIMARY KEY (id)
);

CREATE TABLE tb_product (
	id SERIAL,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(60) NOT NULL,
	price DECIMAL(10,2) NOT NULL,
  active BOOLEAN NOT NULL,
  
  restaurant_id BIGINT NOT NULL,

	PRIMARY KEY (id)
);

CREATE TABLE tb_payment_method (
	id SERIAL,
	description VARCHAR(60) NOT NULL,


	restaurant_id BIGINT NOT NULL,

	PRIMARY KEY (id)
);

CREATE TABLE restaurant_payment_method (
	restaurant_id BIGINT NOT NULL,
	payment_method_id BIGINT NOT NULL,
	
	PRIMARY KEY (restaurant_id, payment_method_id)
);

CREATE TABLE tb_user (
  id SERIAL,
  name VARCHAR(60) NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(60) NOT NULL, 
  confirmed  BOOLEAN DEFAULT FALSE,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY(id)
);

CREATE TABLE tb_group (
	id SERIAL,
	name VARCHAR(60) NOT NULL,

	PRIMARY KEY (id)
);

CREATE TABLE tb_permission (
	id SERIAL,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(60) NOT NULL,

	PRIMARY KEY (id)
);

CREATE TABLE group_permission (
	group_id BIGINT NOT NULL,
	permission_id BIGINT NOT NULL,
	
	PRIMARY KEY (group_id, permission_id)
);

CREATE TABLE user_group (
	user_id BIGINT NOT NULL,
	group_id BIGINT NOT NULL,
	
	PRIMARY KEY (user_id, group_id)
);

CREATE TABLE tb_order (
  id SERIAL,
  subtotal DECIMAL(10,2) NOT NULL,
  shipping_fee DECIMAL(10,2) NOT NULL,
  total DECIMAL(10,2) NOT NULL,

  restaurant_id BIGINT NOT NULL,
  user_client_id BIGINT NOT NULL,
  payment_method_id BIGINT NOT NULL,
  
  addres_city_id BIGINT,
	addres_postal_code VARCHAR(9),
	addres_street VARCHAR(100),
	addres_number VARCHAR(20),
	addres_complement VARCHAR(60),
	addres_district VARCHAR(60),
	
  status VARCHAR(10) NOT NULL,
  created_date TIMESTAMP NOT NULL,
  confirmed_date TIMESTAMP NULL,
  cancel_date TIMESTAMP NULL,
  delivery_date TIMESTAMP NULL,

  PRIMARY KEY (id)
);

create table tb_order_item (
  id SERIAL,
  quantity INT NOT NULL,
  unity_price DECIMAL(10,2) NOT NULL,
  total_price DECIMAL(10,2) NOT NULL,
  note VARCHAR(255) NULL,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  
  PRIMARY KEY (id)
);

ALTER TABLE tb_city ADD constraint fk_city_state
FOREIGN KEY (state_id) references tb_state (id);

ALTER TABLE group_permission ADD constraint fk_group_permission_permission
FOREIGN KEY (permission_id) references tb_permission (id);

ALTER TABLE group_permission ADD constraint fk_group_permission_group
FOREIGN KEY (group_id) references tb_group (id);

ALTER TABLE tb_product ADD constraint fk_product_restaurant
FOREIGN KEY (restaurant_id) references tb_restaurant (id);

ALTER TABLE tb_restaurant ADD constraint fk_restaurant_cuisine
FOREIGN KEY (cuisine_id) references tb_cuisine (id);

ALTER TABLE tb_restaurant ADD constraint fk_restaurant_city
FOREIGN KEY (addres_city_id) references tb_city (id);

ALTER TABLE restaurant_payment_method add constraint fk_restaurant_payment_method_payment_method
FOREIGN KEY (payment_method_id) references tb_payment_method (id);

ALTER TABLE restaurant_payment_method add constraint fk_restaurant_payment_method_restaurant
FOREIGN KEY (restaurant_id) references tb_restaurant (id);

ALTER TABLE user_group ADD constraint fk_user_group_group
FOREIGN KEY (group_id) references tb_group (id);

ALTER TABLE user_group ADD constraint fk_user_group_user
foreign KEY (user_id) references tb_user (id);

ALTER TABLE tb_order ADD constraint fk_order_restaurant 
FOREIGN KEY (restaurant_id) references tb_restaurant (id);

ALTER TABLE tb_order ADD constraint fk_order_usser_client
FOREIGN KEY (user_client_id) references tb_user (id);

ALTER TABLE tb_order ADD constraint fk_order_payment_method 
FOREIGN KEY (payment_method_id) references tb_payment_method (id);

ALTER TABLE tb_order_item ADD constraint fk_order_item_order
FOREIGN KEY (order_id) references tb_order (id);

ALTER TABLE tb_order_item ADD constraint fk_order_item_product 
FOREIGN KEY (product_id) references tb_product (id);

ALTER TABLE tb_order_item ADD constraint uk_order_item_product
UNIQUE (order_id, product_id);
