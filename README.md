# transaction
Microsserviço de transações do Bagual Bank: depósitos, saques e transferência entre contas.

## Sobre o serviço
Ao criar uma transação, o serviço chama o `account` via REST para debitar/creditar o saldo e publica um evento no Kafka para o `notification` de forma assíncrona.
Transferências utilizam um padrão de compensação: se o débito na conta de origem for bem-sucedido mas o crédito na conta de destino falhar, o valor é devolvido automaticamente à origem.

## Funcionalidades implementadas
* Criar transação (`POST /transactions`)
* Buscar transação por id (`GET /transactions/{id}`)
* Listar transações (`GET /transactions`)
* Integração real com o account (débito/crédito via REST)
* Compensação automática em falha de transferência (padrão Saga)
* Validação de dados de entrada
* Tratamento centralizado de erros, incluindo erros propagados pelo account
* Migração de schema com Flyway
* Testes unitários (regra de negócio, cenários de falha e compensação)
* Containerização completa (aplicação + banco via Docker Compose)

## Tecnologias
- Java 21 + Spring Boot 3;
- Maven;
- PostgreSQL;
- Flyway;
- Apache Kafka (Spring Kafka);
- JUnit 5, Mockito e AssertJ;
- Docker e Docker Compose.

## Decisões técnicas
* **Rich Domain Model:** regras de negócio vivem na entidade Transaction;
* **Saga com compensação:** transferências que falham no meio do caminho são revertidas automaticamente, evitando inconsistência de saldo entre contas;
* **Eventos com campo em String, não enum:** o `TransactionEvent` usa String para tipo e status, evitando acoplamento entre os enums do `transaction` e do `notification`, que são projetos independentes;
* **Kafka com listeners separados:** o broker expõe um listener externo e um interno, permitindo que `transaction` e `notification` se comuniquem de forma confiável mesmo estando containerizados.

## Como executar

Pré-requisito: Docker Desktop instalado e em execução, e o [account](https://github.com/laressamoraes/bagual-account) rodando na porta 8081.
O Kafka está definido neste `docker-compose.yml` e precisa subir antes do `notification`!

```bash
docker compose up --build -d
```

A API fica disponível em: http://localhost:8082

## Rodando os testes

```bash
mvn test
```
