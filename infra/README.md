# Infraestrutura do TP5

O Docker Compose reúne aplicações, bancos, RabbitMQ e observabilidade.

```powershell
docker compose up -d --build
docker compose ps
```

Para parar:

```powershell
docker compose down
```

Para remover também volumes de desenvolvimento:

```powershell
docker compose down -v
```
