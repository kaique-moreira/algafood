ALTER TABLE tb_restaurant ADD active  BOOLEAN;
update tb_restaurant SET  active = true;
ALTER TABLE tb_restaurant ALTER COLUMN active SET NOT NULL;
