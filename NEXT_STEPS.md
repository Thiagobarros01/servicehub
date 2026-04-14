# Next Steps — ServiceHub

## Estado atual

- Estou implementando o `AgendamentoService`
- Saí do CRUD e entrei em regra de negócio real
- Já valido:
  - empresa
  - cliente por empresa
  - serviço por empresa
- Já comecei a tratar conflito de horário
- Já uso exceções de negócio
- Uso `Instant` como padrão de data
- O banco ainda possui apenas `dataHoraInicio`
- O cálculo de fim está sendo feito em memória (não persistido)

---

## Decisões atuais

- Não persistir `dataHoraFim` por enquanto
- Resolver conflito no service
- Repository apenas retorna candidatos
- Service decide a regra

---

## Próxima decisão importante

- Persistir ou não `dataHoraFim` futuramente

---

## Próximos testes a implementar

- [ ] erro quando empresa não existir
- [ ] erro quando cliente não existir
- [ ] erro quando serviço não existir
- [ ] erro quando data for no passado
- [ ] erro quando houver conflito de horário

---

## Próximos passos no código

- revisar AgendamentoService com calma
- melhorar clareza do código
- garantir consistência entre modelagem e regra
- evitar dependências desnecessárias

---

## Pontos de atenção

- evitar desalinhamento entre entidade, repository e service
- manter padrão de data consistente
- não misturar duas abordagens ao mesmo tempo
- garantir que regra fique no service

---

## Lembrete importante

- Repository NÃO decide regra
- Service é o coração da regra de negócio
- Teste deve validar comportamento, não implementação