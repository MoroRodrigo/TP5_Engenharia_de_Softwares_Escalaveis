$ErrorActionPreference = "Stop"

Push-Location "$PSScriptRoot\..\infra"
docker compose up -d --build
Write-Host "\nServicos:" -ForegroundColor Cyan
docker compose ps
Write-Host "\nURLs:" -ForegroundColor Cyan
Write-Host "Frontend:    http://localhost:3001"
Write-Host "Backend:     http://localhost:8080/actuator/health"
Write-Host "Inventory:   http://localhost:8081/actuator/health"
Write-Host "RabbitMQ:    http://localhost:15672"
Write-Host "Prometheus:  http://localhost:9090"
Write-Host "Grafana:     http://localhost:3000"
Write-Host "Tempo:       http://localhost:3200"
Write-Host "Loki:        http://localhost:3100/ready"
Pop-Location
