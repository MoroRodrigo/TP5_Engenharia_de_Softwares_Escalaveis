$ErrorActionPreference = "Stop"

# Sempre executar os comandos a partir da raiz do projeto,
# independentemente de onde o script foi chamado.
$ProjectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $ProjectRoot

Write-Host "Projeto: $ProjectRoot" -ForegroundColor DarkGray

if (-not (Get-Command minikube -ErrorAction SilentlyContinue)) {
    throw "Minikube não foi encontrado no PATH. Instale o Minikube e abra um novo PowerShell."
}

if (-not (Get-Command kubectl -ErrorAction SilentlyContinue)) {
    throw "kubectl não foi encontrado no PATH. Instale o kubectl e abra um novo PowerShell."
}

Write-Host "Iniciando/verificando cluster Minikube..." -ForegroundColor Cyan
minikube start -p minikube --driver=docker

Write-Host "Habilitando metrics-server..." -ForegroundColor Cyan
minikube addons enable metrics-server -p minikube

Write-Host "Construindo imagens dentro do Minikube..." -ForegroundColor Cyan
minikube image build -p minikube -t tp5-backend:local "$ProjectRoot\backend"
minikube image build -p minikube -t tp5-inventory-service:local "$ProjectRoot\inventory-service"
minikube image build -p minikube -t tp5-frontend:local "$ProjectRoot"

Write-Host "Aplicando manifests Kubernetes..." -ForegroundColor Cyan
kubectl apply -k "$ProjectRoot\k8s"

Write-Host "Aguardando deployments..." -ForegroundColor Cyan
kubectl rollout status deployment/backend -n tp5 --timeout=180s
kubectl rollout status deployment/inventory-service -n tp5 --timeout=180s
kubectl rollout status deployment/frontend -n tp5 --timeout=180s

Write-Host "`nPods:" -ForegroundColor Cyan
kubectl get pods -n tp5 -o wide

Write-Host "`nServices:" -ForegroundColor Cyan
kubectl get svc -n tp5

Write-Host "`nHPA:" -ForegroundColor Cyan
kubectl get hpa -n tp5

Write-Host "`nFrontend URL:" -ForegroundColor Cyan
minikube service frontend -n tp5 --url -p minikube
