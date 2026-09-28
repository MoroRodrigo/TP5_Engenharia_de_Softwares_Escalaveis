$ErrorActionPreference = "Stop"
Push-Location "$PSScriptRoot\..\infra"
docker compose down
Pop-Location
