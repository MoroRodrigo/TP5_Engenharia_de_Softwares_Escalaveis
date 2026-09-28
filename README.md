# Sistema de Gestão de Produtos e Estoque - TP5

Projeto integrado da disciplina Engenharia de Softwares Escaláveis. Nesta etapa final eu preparei a aplicação desenvolvida nos TPs anteriores para uma operação simulada de produção.

A arquitetura continua orientada a eventos com RabbitMQ, mas agora monólito e inventory-service são executados como containers, possuem PostgreSQL separado, disponibilizam health checks e métricas e enviam logs e traces para uma stack de observabilidade.

## Estrutura

```text
backend/             Monólito Spring Boot + PostgreSQL
inventory-service/   Microsserviço de inventário + PostgreSQL
src/                 Frontend React
infra/               Docker Compose e observabilidade
k8s/                 Kubernetes + HPA
scripts/             Automação local
.github/workflows/   CI/CD com GitHub Actions
docs/                Documentação do TP5
```

## Requisitos

Java 21, Maven 3.9+, Node 20+, Git, Docker Desktop e, para Kubernetes local, Minikube e kubectl.

## Execução com Docker Compose

```powershell
cd infra
docker compose up -d --build
```

Aplicação: `http://localhost:3001`

RabbitMQ: `http://localhost:15672`

Grafana: `http://localhost:3000`

Prometheus: `http://localhost:9090`

Tempo: `http://localhost:3200`

## Execução com Kubernetes

```powershell
cd scripts
.\k8s-local.ps1
```

O script prepara o Minikube, habilita o Metrics Server, constrói as imagens e aplica o Kustomize.

## Testes

```powershell
cd backend
mvn clean test
```

```powershell
cd ..\inventory-service
mvn clean test
```

```powershell
cd ..
npm ci
npm test -- --watchAll=false
```

Ou use `scripts/run-tests.ps1`.

## Observabilidade

O backend e o inventory-service expõem `/actuator/health` e `/actuator/prometheus`. O Prometheus coleta as métricas. O Alloy coleta logs e os envia para Loki. O tracing é enviado por OTLP para Tempo. O Grafana possui datasources provisionados e um dashboard básico do TP5.

## CI/CD

`ci.yml` testa backend, inventory-service e frontend e valida a construção das imagens. `cd.yml` publica as imagens no GHCR e demonstra o fluxo de implantação em Kubernetes. Para usar o CD em um repositório real, é necessário configurar o environment `production` e o secret `KUBE_CONFIG_B64`.

## Continuidade do projeto

No TP1 eu tinha uma aplicação mais simples e a entrega tinha o problema de o backend não aparecer no repositório. A partir do TP2 a persistência foi incorporada ao projeto, no TP3 entrou o microsserviço de inventário, no TP4 a comunicação foi refatorada para RabbitMQ e, neste TP5, todo esse conjunto foi preparado para execução conteinerizada, observável e automatizada.
