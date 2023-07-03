ALTER TABLE tb_restaurant ADD opened  BOOLEAN;
update tb_restaurant SET  opened = true;
ALTER TABLE tb_restaurant ALTER COLUMN opened SET NOT NULL;


