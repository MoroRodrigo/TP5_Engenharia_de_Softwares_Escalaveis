# Backend do sistema

No TP5 este módulo continua sendo o monólito responsável pelo cadastro de produtos e pela projeção do estoque. Em execução local ele pode usar H2; no Docker Compose e no Kubernetes a configuração troca o banco para PostgreSQL por variáveis de ambiente. O módulo possui Actuator, métricas Prometheus, tracing com OpenTelemetry e logs estruturados. A comunicação de negócio com o inventory-service permanece orientada a eventos por RabbitMQ.

Para executar localmente:

```powershell
mvn clean spring-boot:run
```

Health: `http://localhost:8080/actuator/health`
Prometheus: `http://localhost:8080/actuator/prometheus`
