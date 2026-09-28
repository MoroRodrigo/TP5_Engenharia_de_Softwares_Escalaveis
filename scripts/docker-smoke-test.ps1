$ErrorActionPreference = "Stop"

function Test-Url($url, $name) {
    try {
        $response = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 10
        if ($response.StatusCode -ge 200 -and $response.StatusCode -lt 400) {
            Write-Host "OK  $name -> $($response.StatusCode)" -ForegroundColor Green
        } else {
            throw "HTTP $($response.StatusCode)"
        }
    } catch {
        Write-Host "FAIL $name -> $($_.Exception.Message)" -ForegroundColor Red
        exit 1
    }
}

Test-Url "http://localhost:8080/actuator/health" "backend"
Test-Url "http://localhost:8081/actuator/health" "inventory-service"
Test-Url "http://localhost:3001/health" "frontend"
Test-Url "http://localhost:3000/api/health" "grafana"
Test-Url "http://localhost:9090/-/ready" "prometheus"
Test-Url "http://localhost:3100/ready" "loki"
Test-Url "http://localhost:3200/ready" "tempo"

Write-Host "Smoke test concluido." -ForegroundColor Green
