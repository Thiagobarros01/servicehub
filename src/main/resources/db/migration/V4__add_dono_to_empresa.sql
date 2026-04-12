ALTER TABLE empresa
ADD COLUMN dono_id BIGINT;

ALTER TABLE empresa
ADD CONSTRAINT fk_empresa_dono
FOREIGN KEY (dono_id)
REFERENCES usuario(id);