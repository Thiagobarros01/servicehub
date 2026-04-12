CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY ,
    nome VARCHAR(255) NOT NULL ,
    email VARCHAR(255) NOT NULL,
    data_nascimento DATE ,
    empresa_id BIGINT NOT NULL,

        CONSTRAINT fk_cliente_empresa
             FOREIGN KEY(empresa_id)
                 REFERENCES empresa(id),

        CONSTRAINT  uk_cliente_email_empresa
        UNIQUE (empresa_id, email )
);

CREATE INDEX idx_cliente_empresa
ON cliente(empresa_id);
