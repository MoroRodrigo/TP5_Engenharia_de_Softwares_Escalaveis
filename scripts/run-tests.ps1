$ErrorActionPreference = "Stop"

Write-Host "== Backend tests ==" -ForegroundColor Cyan
Push-Location "$PSScriptRoot\..\backend"
mvn clean test
Pop-Location

Write-Host "== Inventory tests ==" -ForegroundColor Cyan
Push-Location "$PSScriptRoot\..\inventory-service"
mvn clean test
Pop-Location

Write-Host "== Front-end dependencies ==" -ForegroundColor Cyan
Push-Location "$PSScriptRoot\.."
if (-not (Test-Path ".\node_modules\.bin\react-scripts.cmd")) {
    Write-Host "node_modules não encontrado ou react-scripts ausente. Executando npm ci..." -ForegroundColor Yellow
    npm ci
}

Write-Host "== Front-end tests ==" -ForegroundColor Cyan
npm test -- --watchAll=false
Pop-Location

Write-Host "Todos os testes terminaram." -ForegroundColor Green
