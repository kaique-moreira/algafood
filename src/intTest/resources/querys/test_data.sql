INSERT INTO tb_cuisine(name)
VALUES 
  ('Francesa') ,
  ('Hamburgueria') ,
  ('Brasileira');

INSERT INTO tb_state (name)
VALUES
  ('Minas Gerais'),
  ('São Paulo'),
  ('Rio de Janeiro'); 

INSERT INTO tb_city (name, state_id)
VALUES
  ('São Paulo', (SELECT id FROM tb_state WHERE name = 'São Paulo')),
  ('Belo Horizonte', (SELECT id FROM tb_state WHERE name = 'Minas Gerais')),
  ('Betim', (SELECT id FROM tb_state WHERE name = 'Minas Gerais'));
INSERT INTO tb_restaurant (
  name,
  shipping_fee,
  addres_city_id, 
  addres_postal_code, 
  addres_street, 
  addres_number, 
  addres_complement, 
  addres_district, 
  cuisine_id
) VALUES
  (
    'Le President',
    20,
    (SELECT id FROM tb_city where name = 'São Paulo'),
    '01416-001',
    'R. da Consolação, 3527',
    '3527',
    '',
    'Cerqueira César',
    (SELECT id FROM tb_cuisine where name = 'Francesa')
  ),
  (
    'Cão veio',
    20,
    (SELECT id FROM tb_city where name = 'São Paulo'),
    '03310-000',
    'R. Itapura',
    '1534',
    '',
    'Vila Gomes Cardim',
    (SELECT id FROM tb_cuisine where name = 'Francesa')
  ), 
  (
    'Test',
    20,
    (SELECT id FROM tb_city where name = 'São Paulo'),
    '03310-000',
    'R. Itapura',
    '1534',
    '',
    'Vila Gomes Cardim',
    (SELECT id FROM tb_cuisine where name = 'Francesa')
  );
INSERT INTO tb_payment_method (
  description
) VALUES
  ('Debito'),
  ('Credito'),
  ('Pix'),
  ('Dinheiro');

INSERT INTO restaurant_payment_method (
  restaurant_id,
  payment_method_id
) VALUES
  (
    (SELECT id FROM tb_restaurant WHERE name = 'Le President'), 
    (SELECT id FROM tb_payment_method WHERE description = 'Debito')
  ),
  (
    (SELECT id FROM tb_restaurant WHERE name = 'Le President'), 
    (SELECT id FROM tb_payment_method WHERE description = 'Credito')
  ),  
  (
    (SELECT id FROM tb_restaurant WHERE name = 'Le President'), 
    (SELECT id FROM tb_payment_method WHERE description = 'Pix')
  ),
  (
    (SELECT id FROM tb_restaurant WHERE name = 'Le President'), 
    (SELECT id FROM tb_payment_method WHERE description = 'Dinheiro')
  ),
  (
    (SELECT id FROM tb_restaurant WHERE name = 'Cão veio'), 
    (SELECT id FROM tb_payment_method WHERE description = 'Debito')
  ),
  (
    (SELECT id FROM tb_restaurant WHERE name = 'Cão veio'), 
    (SELECT id FROM tb_payment_method WHERE description = 'Credito')
  ),  
  (
    (SELECT id FROM tb_restaurant WHERE name = 'Cão veio'), 
    (SELECT id FROM tb_payment_method WHERE description = 'Pix')
  ),
  (
    (SELECT id FROM tb_restaurant WHERE name = 'Cão veio'), 
    (SELECT id FROM tb_payment_method WHERE description = 'Dinheiro')
  );

INSERT INTO tb_product (
  name,
  description,
  price, 
  active,
  restaurant_id
) VALUES 
  (
    'test1',
    'test1',
    10.10,
    true,  
    (SELECT id FROM tb_restaurant WHERE name = 'Le President')
   ),
  (
    'test2',
    'test2',
    10.10,
    true,  
    (SELECT id FROM tb_restaurant WHERE name = 'Cão veio')
  );

INSERT INTO tb_user (
  name,
  email,
  password,
  confirmed
) VALUES
  (
    'user1',
    'user1@gmail.com',
    'user1123',
    true
  ),
  (
    'user2',
    'user2@gmail.com',
    'user2123',
    true
  ),
  (
    'user3',
    'user3@gmail.com',
    'user3123',
    true
  );
   
INSERT INTO tb_permission (
  name,
  description
) VALUES
  (
    'Make Order',
    ''
  ),
  (
    'Create Restaurant',
    ''
  );

INSERT INTO tb_group (
  name
) VALUES 
  ('Client'),
  ('Restaurant');

INSERT INTO group_permission (
  group_id,
  permission_id
) VALUES 
  (
    (SELECT id FROM tb_group WHERE name = 'Client'), 
    (SELECT id FROM tb_permission WHERE name = 'Make Order')
  ),
  (
    (SELECT id FROM tb_group WHERE name = 'Restaurant'), 
    (SELECT id FROM tb_permission WHERE name = 'Create Restaurant')
  );

INSERT INTO user_group (
  user_id,
  group_id
) VALUES 
  (
    (SELECT id FROM tb_user WHERE name = 'user1'), 
    (SELECT id FROM tb_group WHERE name = 'Restaurant')
  ),
  (
    (SELECT id FROM tb_user WHERE name = 'user2'), 
    (SELECT id FROM tb_group WHERE name = 'Client') 
  ),
  (
    (SELECT id FROM tb_user WHERE name = 'user3'), 
    (SELECT id FROM tb_group WHERE name = 'Client')
  );

INSERT INTO tb_order (
  subtotal,
  shipping_fee,
  total,
  restaurant_id,
  user_client_id,
  payment_method_id,
  addres_city_id,
  addres_postal_code,
  addres_street,
  addres_number,
  addres_complement,
  status,
  confirmed_date,
  cancel_date,
  delivery_date
) VALUES
  (
    10.00,
    10.00,
    10.00,
    (SELECT id FROM tb_restaurant WHERE name = 'Le President'), 
    (SELECT id FROM tb_user WHERE name = 'user1'), 
    (SELECT id FROM tb_payment_method WHERE description = 'Pix'), 
    (SELECT id FROM tb_city WHERE name = 'São Paulo'), 
    '',
    '',
    5,
    '',
    '',
    null,
    null,
    null
  ),
  (
    10.00,
    10.00,
    10.00,
    (SELECT id FROM tb_restaurant WHERE name = 'Le President'), 
    (SELECT id FROM tb_user WHERE name = 'user3'), 
    (SELECT id FROM tb_payment_method WHERE description =  'Debito'), 
    (SELECT id FROM tb_city WHERE name = ''), 
    '',
    '',
    5,
    '',
    '',
    null,
    null,
    null
  );

INSERT INTO tb_order_item (
  quantity,
  unity_price,
  total_price,
  note,
  order_id,
  product_id
) VALUES 
  (
    10,
    10.00,
    100.00,
    '',
    (SELECT id FROM tb_order
      WHERE user_client_id = (SELECT id FROM tb_user WHERE name = 'user1') 
      AND restaurant_id = (SELECT id FROM tb_restaurant WHERE name = 'Le President')), 
    (SELECT id FROM tb_product WHERE name = 'test1')
  ),
  (
    10,
    10.00,
    100.00,
    '',
    (SELECT id FROM tb_order
      WHERE user_client_id = (SELECT id FROM tb_user WHERE name = 'user3') 
      AND restaurant_id = (SELECT id FROM tb_restaurant WHERE name = 'Le President')), 
    (SELECT id FROM tb_product WHERE name = 'test2') 
  ),
  (
    10,
    10.00,
    100.00,
    '',
    (SELECT id FROM tb_order
      WHERE user_client_id = (SELECT id FROM tb_user WHERE name = 'user3') 
      AND restaurant_id = (SELECT id FROM tb_restaurant WHERE name = 'Le President')), 
    (SELECT id FROM tb_product WHERE name = 'test1')
  );
