# Technical Decisions — ServiceHub

## Decisão 1 — Regra de conflito de horário

### Situação

O sistema precisa validar conflito de agendamento.

### Opções

1. Persistir `dataHoraFim`
2. Calcular em memória

### Decisão atual

- Calcular `dataHoraFim` em memória
- Usar duração do serviço
- Buscar candidatos no repository
- Resolver conflito no service

### Motivo

- Banco ainda não possui `dataHoraFim`
- Permite evoluir sem alterar migration agora
- Foco no aprendizado de regra de negócio

### Limitação

- Consulta menos eficiente
- Lógica mais complexa no service

### Evolução futura

- Adicionar `dataHoraFim` no banco
- Mover parte da lógica para query

---

## Decisão 2 — Local da regra de negócio

### Decisão

- Regra fica no Service

### Motivo

- Centraliza comportamento
- Evita controller inchado
- Facilita testes

---

## Decisão 3 — Testes

### Decisão

- Testar comportamento
- Não testar repository diretamente

### Motivo

- Garantir regra de negócio
- Isolar dependências
- Aumentar confiabilidade

---

## Decisão 4 — Uso de Clock

### Decisão

- Usar `Clock` nos testes

### Motivo

- Tornar testes determinísticos
- Evitar dependência do horário real

---

## Decisão 5 — Estratégia de evolução

### Decisão

- Não usar complexidade cedo
- Evoluir por etapas

### Ordem

1. Base
2. Regra
3. Teste
4. Segurança
5. Arquitetura