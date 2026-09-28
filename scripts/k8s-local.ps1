$ErrorActionPreference = "Stop"

minikube status | Out-Null
if ($LASTEXITCODE -ne 0) {
    minikube start --driver=docker
}

minikube addons enable metrics-server

Write-Host "Construindo imagens dentro do Minikube..." -ForegroundColor Cyan
minikube image build -t tp5-backend:local ./backend
minikube image build -t tp5-inventory-service:local ./inventory-service
minikube image build -t tp5-frontend:local .

Write-Host "Aplicando manifests..." -ForegroundColor Cyan
kubectl apply -k k8s

kubectl rollout status deployment/backend -n tp5 --timeout=180s
kubectl rollout status deployment/inventory-service -n tp5 --timeout=180s
kubectl rollout status deployment/frontend -n tp5 --timeout=180s

Write-Host "\nPods:" -ForegroundColor Cyan
kubectl get pods -n tp5

Write-Host "\nHPA:" -ForegroundColor Cyan
kubectl get hpa -n tp5

Write-Host "\nFrontend URL:" -ForegroundColor Cyan
minikube service frontend -n tp5 --url
