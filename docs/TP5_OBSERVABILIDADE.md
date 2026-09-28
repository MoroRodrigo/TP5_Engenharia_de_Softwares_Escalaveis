# TP5 - Observabilidade

## Visão geral

Eu organizei a observabilidade em três sinais principais: métricas, logs e traces. O Spring Boot Actuator publica métricas para o Prometheus. O Grafana Alloy coleta logs dos containers e envia para Loki. O Spring Boot usa Micrometer Tracing com OpenTelemetry e exporta traces por OTLP para o Tempo.

## Métricas

Os serviços expõem `/actuator/prometheus`. O Prometheus faz scraping a cada dez segundos. O dashboard do Grafana usa as métricas de requisições e memória JVM para acompanhar o comportamento das aplicações.

## Logs

No Docker Compose, o Alloy observa o Docker socket e utiliza `loki.source.docker` para encaminhar logs a Loki. No Kubernetes, o Alloy roda como DaemonSet e utiliza `loki.source.kubernetes` para acompanhar os logs dos Pods.

## Tracing

O backend e o inventory-service estão configurados para gerar traces e exportá-los para o Tempo por OTLP HTTP. Como a comunicação de negócio continua passando por RabbitMQ, os logs registram `eventId`, `eventType` e `correlationId`, facilitando a associação do processamento de uma mensagem com o restante da operação.

## Acesso local

Grafana: `http://localhost:3000`

Prometheus: `http://localhost:9090`

Loki: `http://localhost:3100/ready`

Tempo: `http://localhost:3200`
