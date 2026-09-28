# Inventory Service

Microsserviço responsável pelo domínio de inventário. No TP5 ele pode executar localmente com H2 e, em ambiente conteinerizado, utiliza PostgreSQL próprio. Os comandos de movimentação continuam chegando pelo RabbitMQ e os eventos de resultado retornam ao monólito. O serviço expõe Actuator, métricas Prometheus, logs estruturados e tracing com OpenTelemetry.

Para executar localmente:

```powershell
mvn clean spring-boot:run
```

Health: `http://localhost:8081/actuator/health`
Prometheus: `http://localhost:8081/actuator/prometheus`
