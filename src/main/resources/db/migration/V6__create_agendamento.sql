CREATE TABLE agendamento (
                             id BIGSERIAL PRIMARY KEY,
                             data_hora_inicio TIMESTAMP NOT NULL,
                             cliente_id BIGINT NOT NULL,
                             servico_id BIGINT NOT NULL,
                             empresa_id BIGINT NOT NULL,
                             status VARCHAR(20) NOT NULL,

                             CONSTRAINT fk_agendamento_cliente
                                 FOREIGN KEY (cliente_id)
                                     REFERENCES cliente(id),

                             CONSTRAINT fk_agendamento_servico
                                 FOREIGN KEY (servico_id)
                                     REFERENCES servico(id),

                             CONSTRAINT fk_agendamento_empresa
                                 FOREIGN KEY (empresa_id)
                                     REFERENCES empresa(id),

                             CHECK (status IN ('PENDENTE', 'CONFIRMADO', 'CANCELADO', 'FINALIZADO'))
);

CREATE INDEX idx_agendamento_empresa
    ON agendamento(empresa_id);

CREATE INDEX idx_agendamento_data
    ON agendamento(data_hora_inicio);