CREATE TABLE pagamento (
    id BIGSERIAL PRIMARY KEY,
    agendamento_id BIGINT NOT NULL,
    valor NUMERIC(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    metodo VARCHAR(20) NOT NULL,
    data_pagamento TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_pagamento_agendamento
        FOREIGN KEY (agendamento_id)
            REFERENCES agendamento(id),

    CONSTRAINT uk_pagamento_agendamento
        UNIQUE (agendamento_id),

    CHECK (valor > 0),
    CHECK (status IN ('PENDENTE', 'PAGO', 'FALHOU', 'CANCELADO')),
    CHECK (metodo IN ('PIX', 'CARTAO', 'DINHEIRO'))
);