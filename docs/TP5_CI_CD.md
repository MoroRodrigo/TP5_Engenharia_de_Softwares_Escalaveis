# TP5 - CI/CD

## CI

O workflow `.github/workflows/ci.yml` é executado em pull requests e pushes para `main`. Ele testa separadamente o backend, o inventory-service e o frontend. Depois dos testes, os três Dockerfiles são construídos para verificar se as imagens são montáveis.

## CD

O workflow `.github/workflows/cd.yml` reaproveita a etapa de testes, publica as três imagens no GitHub Container Registry usando tags relacionadas ao commit e executa uma etapa de implantação em um environment chamado `production`.

A implantação depende de um secret chamado `KUBE_CONFIG_B64`, contendo o kubeconfig codificado em Base64. O workflow aplica os manifests, atualiza as imagens para a versão do commit e aguarda os rollouts. Para um cluster real, eu também configuraria regras de proteção do environment e credenciais específicas para pull das imagens privadas.

## Versionamento

A raiz do projeto mantém `VERSION` e `CHANGELOG.md`. A intenção é que cada alteração relevante do projeto tenha um commit descritivo e que o GitHub seja a fonte do código versionado e do histórico de execução das pipelines.
