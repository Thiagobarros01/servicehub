CREATE TABLE servico (
    id BIGSERIAL PRIMARY KEY ,
    nome VARCHAR(255) NOT NULL ,
    duracao_minutos INT NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    empresa_id BIGINT NOT NULL,

    CONSTRAINT fk_servico_empresa
    FOREIGN KEY (empresa_id)
    REFERENCES empresa(id),

    CONSTRAINT uk_servico_nome_empresa
        UNIQUE  (empresa_id, nome),

    CHECK ( duracao_minutos > 0 ),
    CHECK ( preco >= 0 )
);


CREATE INDEX idx_servico_empresa
    ON servico(empresa_id);