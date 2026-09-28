$ErrorActionPreference = "Stop"

Write-Host "Iniciando carga simples no backend. Pressione Ctrl+C para encerrar." -ForegroundColor Yellow

kubectl -n tp5 run tp5-load-generator --rm -it --restart=Never --image=busybox:1.36 -- /bin/sh -c "for i in 1 2 3 4 5 6 7 8; do (while true; do wget -q -O - http://backend:8080/actuator/health > /dev/null; done) & done; wait"
