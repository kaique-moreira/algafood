delete from restaurant_payment_method cascade;
delete from restaurant_user cascade;
delete from user_group cascade;
delete from group_permission cascade;
delete from tb_order_item cascade;
delete from tb_order cascade;
delete from tb_user cascade;
delete from tb_product cascade;
delete from tb_restaurant cascade;
delete from tb_cuisine cascade;
delete from tb_city cascade;
delete from tb_state cascade;
delete from tb_permission cascade;
delete from tb_group cascade;
delete from tb_payment_method cascade;

alter sequence  tb_order_item_id_seq restart with 1;
alter sequence  tb_order_id_seq restart with 1;
alter sequence  tb_user_id_seq restart with 1;
alter sequence  tb_product_id_seq restart with 1;
alter sequence  tb_restaurant_id_seq restart with 1;
alter sequence  tb_cuisine_id_seq restart with 1;
alter sequence  tb_city_id_seq restart with 1;
alter sequence  tb_state_id_seq restart with 1;
alter sequence  tb_permission_id_seq restart with 1;
alter sequence  tb_group_id_seq restart with 1;
alter sequence  tb_payment_method_id_seq restart with 1;



insert into tb_cuisine (id, name) values (1, 'Tailandesa');
insert into tb_cuisine (id, name) values (2, 'Indiana');
insert into tb_cuisine (id, name) values (3, 'Argentina');
insert into tb_cuisine (id, name) values (4, 'Brasileira');

insert into tb_state (id, name) values (1, 'Minas Gerais');
insert into tb_state (id, name) values (2, 'São Paulo');
insert into tb_state (id, name) values (3, 'Ceará');

insert into tb_city (id, name, state_id) values (1, 'Uberlândia', 1);
insert into tb_city (id, name, state_id) values (2, 'Belo Horizonte', 1);
insert into tb_city (id, name, state_id) values (3, 'São Paulo', 2);
insert into tb_city (id, name, state_id) values (4, 'Campinas', 2);
insert into tb_city (id, name, state_id) values (5, 'Fortaleza', 3);

insert into tb_restaurant (id, name, shipping_fee, cuisine_id, register_date, update_date, active, opened, addres_city_id, addres_postal_code, addres_street, addres_number, addres_district) values (1, 'Thai Gourmet', 10, 1, NOW(), NOW(), true, true, 1, '38400-999', 'Rua João Pinheiro', '1000', 'Centro');
insert into tb_restaurant (id, name, shipping_fee, cuisine_id, register_date, update_date, active, opened) values (2, 'Thai Delivery', 9.50, 1, NOW(), NOW(), true, true);
insert into tb_restaurant (id, name, shipping_fee, cuisine_id, register_date, update_date, active, opened) values (3, 'Tuk Tuk Comida Indiana', 15, 2, NOW(), NOW(), true, true);
insert into tb_restaurant (id, name, shipping_fee, cuisine_id, register_date, update_date, active, opened) values (4, 'Java Steakhouse', 12, 3, NOW(), NOW(), true, true);
insert into tb_restaurant (id, name, shipping_fee, cuisine_id, register_date, update_date, active, opened) values (5, 'Lanchonete do Tio Sam', 11, 4, NOW(), NOW(), true, true);
insert into tb_restaurant (id, name, shipping_fee, cuisine_id, register_date, update_date, active, opened) values (6, 'Bar da Maria', 6, 4, NOW(), NOW(), true, true);

insert into tb_payment_method (id, description) values (1, 'Cartão de crédito');
insert into tb_payment_method (id, description) values (2, 'Cartão de débito');
insert into tb_payment_method (id, description) values (3, 'Dinheiro');

insert into restaurant_payment_method (restaurant_id, payment_method_id) values (1, 1), (1, 2), (1, 3), (2, 3), (3, 2), (3, 3), (4, 1), (4, 2), (5, 1), (5, 2), (6, 3);

insert into tb_product (id, name, description, price, active, restaurant_id) values (1, 'Porco com molho agridoce', 'Deliciosa carne suína ao molho especial', 78.90, true, 1);
insert into tb_product (id, name, description, price, active, restaurant_id) values (2, 'Camarão tailandês', '16 camarões grandes ao molho picante', 110, true, 1);
insert into tb_product (id, name, description, price, active, restaurant_id) values (3, 'Salada picante com carne grelhada', 'Salada de folhas com cortes finos de carne bovina grelhada e nosso molho especial de pimenta vermelha', 87.20, true, 2);
insert into tb_product (id, name, description, price, active, restaurant_id) values (4, 'Garlic Naan', 'Pão tradicional indiano com cobertura de alho', 21, true, 3);
insert into tb_product (id, name, description, price, active, restaurant_id) values (5, 'Murg Curry', 'Cubos de frango preparados com molho curry e especiarias', 43, true, 3);
insert into tb_product (id, name, description, price, active, restaurant_id) values (6, 'Bife Ancho', 'Corte macio e suculento, com dois dedos de espessura, retirado da parte dianteira do contrafilé', 79, true, 4);
insert into tb_product (id, name, description, price, active, restaurant_id) values (7, 'T-Bone', 'Corte muito saboroso, com um osso em formato de T, sendo de um lado o contrafilé e do outro o filé mignon', 89, true, 4);
insert into tb_product (id, name, description, price, active, restaurant_id) values (8, 'Sanduíche X-Tudo', 'Sandubão com muito queijo, hamburger bovino, bacon, ovo, salada e maionese', 19, true, 5);
insert into tb_product (id, name, description, price, active, restaurant_id) values (9, 'Espetinho de Cupim', 'Acompanha farinha, mandioca e vinagrete', 8, true, 6);

insert into tb_permission (id, name, description) values (1, 'CONSULTAR_cuisineS', 'Permite consultar cuisines');
insert into tb_permission (id, name, description) values (2, 'EDITAR_cuisineS', 'Permite editar cuisines');

insert into tb_group (id, name) values (1, 'Gerente'), (2, 'Vendedor'), (3, 'Secretária'), (4, 'Cadastrador');

insert into group_permission (group_id, permission_id) values (1, 1), (1, 2), (2, 1), (2, 2), (3, 1); 

insert into tb_user (id, name, email, password, register_date) values
(1, 'João da Silva', 'joao.ger@algafood.com', '123', NOW()),
(2, 'Maria Joaquina', 'maria.vnd@algafood.com', '123', NOW()),
(3, 'José Souza', 'jose.aux@algafood.com', '123', NOW()),
(4, 'Sebastião Martins', 'sebastiao.cad@algafood.com', '123', NOW()),
(5, 'Manoel Lima', 'manoel.loja@gmail.com', '123', NOW());

insert into user_group (user_id, group_id) values (1, 1), (1, 2), (2, 2);

insert into restaurant_user (restaurant_id, user_id) values (1, 5), (3, 5);

insert into tb_order (id, code, restaurant_id, user_client_id, payment_method_id, addres_city_id, addres_postal_code, 
    addres_street, addres_number, addres_complement, addres_district,
    status, created_date, subtotal, shipping_fee, total)
values (1, '2986f934-d866-483d-a562-35fa4ec36169', 1, 1, 1, 1, '38400-000', 'Rua Floriano Peixoto', '500', 'Apto 801', 'Brasil',
'CREATED', NOW(), 298.90, 10, 308.90);

insert into tb_order (id, code, restaurant_id, user_client_id, payment_method_id, addres_city_id, addres_postal_code, 
        addres_street, addres_number, addres_complement, addres_district,
        status, created_date, subtotal, shipping_fee, total)
values (2, '6aa69296-ee8c-404e-a3ef-c84de0c88a56', 4, 1, 2, 1, '38400-111', 'Rua Acre', '300', 'Casa 2', 'Centro',
'CREATED', NOW(), 79, 0, 79);

insert into tb_order_item (id, order_id, product_id, quantity, unity_price,total_price, note)
values (3, 2, 6, 1, 79, 79, 'Ao ponto'),
(1, 1, 1, 1, 78.9, 78.9, null),
(2, 1, 2, 2, 110, 220, 'Menos picante, por favor');

select setval('tb_order_id_seq ',10,true);
select setval('tb_order_item_id_seq ',10,true);
select setval('tb_user_id_seq ',10,true);
select setval('tb_product_id_seq ',10,true);
select setval('tb_restaurant_id_seq ',10,true);
select setval('tb_cuisine_id_seq ',10,true);
select setval('tb_city_id_seq ',10,true);
select setval('tb_state_id_seq ',10,true);
select setval('tb_permission_id_seq ',10,true);
select setval('tb_group_id_seq ',10,true);
select setval('tb_payment_method_id_seq ',10,true);


