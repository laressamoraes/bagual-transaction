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
| transaction  | Depósitos, saques e transferências entre contas       | **Implementado** |
| notification | Notificações assíncronas sobre transações realizadas  | **Em andamento** |

## transaction - funcionalidades implementadas
* Criar transação ('POST /transactions')
* Buscar transação por id ('GET /transactions/{id}')
* Listar transações ('GET /transactions')
* Integração real com o Account (débito/crédito via REST)
* Compensação automática em falha de transferência (padrão Saga)
* Validação de dados de entrada
* Tratamento centralizado de erros, incluindo erros propagados pelo Account
* Migração de schema com Flyway
* Testes unitários (regra de negócio, cenários de falha e compensação)
* Containerização completa (aplicação + banco via Docker Compose)

## Como executar

#### Pré-requisito

- Docker Desktop instalado e em execução;
- [account] (https://github.com/laressamoraes/bagual-account) rodando na porta 8081.

#### Subindo a aplicação

Na raiz do projeto, execute:

```bash
docker compose up --build -d
```

A API estará disponível em:

`http://localhost:8082`

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
- RestClient (comunicação síncrona com o Account);
- JUnit 5 e Mockito;
- Docker e Docker Compose.

# Limitações conhecidas
* A compensação da transferência não é garantida caso o próprio passo de compensação falhe;
* A comunicação entre 'transaction' e 'account', quando ambos containerizados, depende de 'host.docker.internal'.
