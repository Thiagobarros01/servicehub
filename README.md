# ServiceHub

Projeto de estudo orientado a mercado para evoluir backend Java/Spring em cima de um sistema SaaS multi-tenant de agendamento.

O objetivo deste projeto nao e so "funcionar".
Ele existe para treinar:
- modelagem de dominio
- banco relacional com Flyway
- arquitetura em camadas
- testes unitarios, web e integracao
- regras de negocio reais
- multi-tenant
- evolucao futura para seguranca, concorrencia, mensageria e integracoes externas

## Estado Atual

Hoje o projeto ja tem:
- migrations Flyway de `Empresa`, `Usuario`, `Cliente`, `Servico`, `Agendamento` e `Pagamento`
- entidades JPA mapeadas
- repositories
- services base
- `AgendamentoService` com regra real
- controllers para `Empresa`, `Cliente`, `Servico` e `Agendamento`
- DTOs e mappers
- tratamento global de excecao
- testes:
  - unitario de service
  - web de controller
  - integracao com Testcontainers/PostgreSQL

## Documentos do Projeto

Usa estes arquivos como guia complementar:
- [PROJECT_CONTEXT.md](PROJECT_CONTEXT.md): contexto geral e intencao do projeto
- [DECISIONS.md](DECISIONS.md): decisoes arquiteturais e tecnicas
- [NEXT_STEPS.md](NEXT_STEPS.md): backlog e proximos passos

## Stack

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Flyway
- PostgreSQL
- JUnit 5
- Mockito
- Testcontainers

## Estrutura

```text
src/main/java/thiagosbarros/com/servicehub
  controller
  controller/dto
  controller/handler
  entity
  entity/enums
  exception
  mapper
  repository
  service
  config

src/main/resources
  db/migration
  application.yml
```

## Como Rodar Localmente

### 1. Subir o PostgreSQL local

O `application.yml` atual aponta para:
- database: `db_servicehub`
- user: `postgres`
- senha: `admin`

Tu pode usar um PostgreSQL local ou Docker.

### 2. Rodar a aplicacao

No diretorio do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

Ou pela IDE, executando `ServicehubApplication`.

### 3. Flyway

Ao subir a aplicacao:
- o Spring conecta no PostgreSQL
- o Flyway roda as migrations automaticamente

## Como Rodar os Testes

### Teste unitario

Exemplo:

```powershell
.\mvnw.cmd -Dtest=AgendamentoServiceTest test
```

### Teste web/controller

Exemplo:

```powershell
.\mvnw.cmd -Dtest=AgendamentoControllerTest test
```

### Teste de integracao com banco real

Antes de rodar:
- abrir o Docker Desktop
- confirmar:

```powershell
docker ps
```

Depois:

```powershell
.\mvnw.cmd -Dtest=AgendamentoIntegrationTest test
```

Esse teste:
- sobe PostgreSQL temporario com Testcontainers
- conecta Spring nesse banco
- roda Flyway
- executa fluxo real

## Endpoints Atuais

### Empresa

- `POST /empresas`
- `GET /empresas/{id}`

### Cliente

- `POST /empresas/{empresaId}/clientes`
- `GET /empresas/{empresaId}/clientes/{id}`

### Servico

- `POST /empresas/{empresaId}/servicos`
- `GET /empresas/{empresaId}/servicos/{id}`

### Agendamento

- `POST /empresas/{empresaId}/agendamentos`

## Contrato de Erro

O projeto possui `GlobalExceptionHandler` com padronizacao de erro.

Status usados hoje:
- `400 Bad Request`: erro de validacao de entrada
- `404 Not Found`: recurso nao encontrado
- `422 Unprocessable Entity`: regra de negocio violada

## Regras Importantes do Projeto

- Service contem regra de negocio
- Controller adapta HTTP
- Mapper converte dominio <-> DTO
- DTO nao deve carregar regra de negocio
- Repository nao deve concentrar regra
- Banco continua sendo ultima barreira de integridade

## Como Evoluir o Projeto

Sempre que houver upgrade relevante, atualizar:

1. `README.md`
- como rodar
- o que existe hoje
- endpoints disponiveis

2. `DECISIONS.md`
- decisao arquitetural
- tradeoff
- motivacao

3. `NEXT_STEPS.md`
- proxima sprint
- backlog curto

4. `PROJECT_CONTEXT.md`
- se o escopo ou direcao do projeto mudar

## Fluxo de Estudo Recomendado

Quando voltar para o projeto:

1. Ler este `README.md`
2. Ver `NEXT_STEPS.md`
3. Revisar `DECISIONS.md`
4. Rodar os testes principais
5. Fazer a proxima entrega

## Proximo Passo Atual

No momento desta versao do README, o foco recomendado e:
- consolidar os testes de integracao
- criar testes web para `EmpresaController`, `ClienteController` e `ServicoController`
- depois partir para autenticacao/JWT

