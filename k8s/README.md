# Kubernetes do TP5

O diretório usa Kustomize. Para ambiente local com Minikube:

```powershell
minikube start --driver=docker
minikube addons enable metrics-server
minikube image build -t tp5-backend:local ./backend
minikube image build -t tp5-inventory-service:local ./inventory-service
minikube image build -t tp5-frontend:local .
kubectl apply -k k8s
```

Acompanhar:

```powershell
kubectl get pods -n tp5
kubectl get hpa -n tp5
kubectl get svc -n tp5
```
