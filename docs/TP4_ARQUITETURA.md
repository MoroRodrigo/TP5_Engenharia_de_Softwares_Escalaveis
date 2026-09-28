# Arquitetura do TP4

## Antes - TP3

```text
React -> Monólito :8080 -> Spring Cloud OpenFeign -> inventory-service :8081
```

O monólito dependia da disponibilidade imediata do `inventory-service` para consultar e registrar o estoque.

## Depois - TP4

```text
React
  |
  v
Monólito :8080
  |
  v
Outbox DB -- Publicador --> RabbitMQ
                              |
                              v
                    inventory-service :8081
                              |
                              v
                     Outbox DB do inventário
                              |
                              v
                         RabbitMQ
                              |
                              v
                    Monólito / projeção
                              |
                              v
                            React
```

## Exchanges

- `produtos.events` - eventos de Produto usando exchange do tipo topic.
- `estoque.commands` - comandos de movimentação usando exchange do tipo direct.
- `estoque.events` - eventos de resultado usando exchange do tipo topic.
- `dead-letter.exchange` - mensagens rejeitadas por falha de processamento.

## Routing keys

- `produto.criado.v1`
- `produto.atualizado.v1`
- `produto.excluido.v1`
- `estoque.movimentacao.solicitada.v1`
- `estoque.movimentacao.registrada.v1`
- `estoque.movimentacao.rejeitada.v1`

## Envelope

O envelope possui `eventId`, `eventType`, `aggregateType`, `aggregateId`, `occurredAt`, `source`, `version`, `correlationId` e `payload`.

## Resiliência

O Outbox grava o evento junto com a operação de negócio e tenta publicar os eventos pendentes periodicamente. Os consumidores gravam o `eventId` processado para evitar duplicidade. As filas possuem dead-letter exchange para mensagens que não conseguem ser processadas.
