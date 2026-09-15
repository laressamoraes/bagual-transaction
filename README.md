# bagual-bank
Sistema financeiro simplificado construído em arquitetura de microsserviços, simulando operações bancárias básicas como criação de conta, depósito, saque e transferências.

No Rio Grande do Sul, bagual é um termo que serve pra descrever um cavalo xucro, selvagem, não domado. Mas quando usado pra se referir a pessoas, representa coragem, resiliência e autenticidade.

# Sobre o projeto
Tem como objetivo aplicar os conceitos e ferramentas utilizados em sistemas corporativos de médio/grande porte: comunicação entre serviços, consistência de dados distribuídos, testes automatizados e containerização.

# Arquitetura
O sistema é dividido em microsserviços independentes:
| SERVIÇO | RESPONSABILIDADE | STATUS |
|---|---|---|
| account      | Cadastro de contas, consulta de saldo, débito/crédito | **Implementado** |
| transaction  | Depósitos, saques e transferências entre contas       | **Em andamento** |
| notification | Notificações assíncronas sobre transações realizadas  | **Não iniciado** |

## account - funcionalidades implementadas
* Criar conta ('POST /accounts')
* Buscar conta por id ('GET /accounts/{id}')
* Listar contas ('GET /accounts')
* Débito de Saldo ('PATCH /accounts/{id}/debit')
* Crédito de saldo ('PATCH /accounts/{id}/credit')
* Bloqueio/ativação de conta
* Validação de dados de entrada
* Migração de schema com Flyway
* Testes unitários (regra de negócio e service layer)
* Containerização completa (aplicação + banco via Docker Compose)

## Como executar

#### Pré-requisito

- Docker Desktop instalado e em execução.

#### Subindo a aplicação

Na raiz do projeto, execute:

```bash
docker compose up --build -d
```

A API estará disponível em:

`http://localhost:8081`

#### Parando a aplicação

Para parar os containers:

```bash
docker compose down
```

Para parar os containers e remover os dados do banco:

```bash
docker compose down -v
```

## Rodando os testes

Para executar os testes automatizados:

```bash
mvn test
```

# Tecnologias
- Java 21 + Spring Boot 3;
- Maven;
- PostgreSQL;
- Flyway;
- JUnit 5 e Mockito;
- Docker e Docker Compose.
