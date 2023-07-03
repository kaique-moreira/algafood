alter table tb_order rename to oldtable;
alter sequence tb_order_id_seq rename to oldtable_id_seq;

create table tb_order (
  id SERIAL,
  code VARCHAR(36),
  subtotal MONEY NOT NULL,
  shipping_fee MONEY NOT NULL,
  total MONEY NOT NULL,

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
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  confirm_date TIMESTAMP NULL,
  cancel_date TIMESTAMP NULL,
  delivery_date TIMESTAMP NULL,

  PRIMARY KEY (id)
);
insert into tb_order (id,restaurant_id, user_client_id, payment_method_id, addres_city_id, addres_postal_code, 
    addres_street, addres_number, addres_complement, addres_district,
    status, created_date, subtotal, shipping_fee, total) select id, restaurant_id, user_client_id, payment_method_id, addres_city_id, addres_postal_code, 
    addres_street, addres_number, addres_complement, addres_district,
    status, created_date, subtotal, shipping_fee, total from oldtable;

drop table oldtable cascade;
update tb_order set code = gen_random_uuid ();
alter table tb_order alter column code set not null;
alter table tb_order add constraint uk_order_code unique (code) 

