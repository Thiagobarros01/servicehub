CREATE TABLE usuario(
 id BIGSERIAL PRIMARY KEY ,
 nome VARCHAR(255) NOT NULL,
 email VARCHAR(255) NOT NULL,
 senha VARCHAR(255) NOT NULL,
 role VARCHAR(50) NOT NULL,
 empresa_id BIGINT NOT NULL,

    CONSTRAINT fk_usuario_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id),

    CONSTRAINT uk_usuario_email_empresa
    UNIQUE (empresa_id, email)
);

CREATE INDEX idx_usuario_empresa
    ON usuario(empresa_id);