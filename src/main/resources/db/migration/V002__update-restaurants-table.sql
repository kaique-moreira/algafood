ALTER TABLE tb_restaurant ADD active  BOOLEAN NOT NULL;
update tb_restaurant SET  active = true;
