# Cloudflare Tunnel 一键启动脚本（Windows）
# 使用方法: 双击运行 或 在 PowerShell 中执行 .\cloudflare\start-tunnel.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  启动 Cloudflare Tunnel" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# 检查 cloudflared 是否安装
$cloudflared = Get-Command cloudflared -ErrorAction SilentlyContinue
if (-not $cloudflared) {
    Write-Host "[错误] 未检测到 cloudflared，请先安装:" -ForegroundColor Red
    Write-Host "  方式一: winget install Cloudflare.cloudflared" -ForegroundColor Yellow
    Write-Host "  方式二: 下载 https://github.com/cloudflare/cloudflared/releases" -ForegroundColor Yellow
    pause
    exit 1
}

# 检查后端是否运行
$backendRunning = Test-NetConnection -ComputerName localhost -Port 8080 -WarningAction SilentlyContinue -InformationLevel Quiet
if (-not $backendRunning) {
    Write-Host "[警告] 后端未运行，请先启动 Spring Boot" -ForegroundColor Yellow
    Write-Host "  执行: cd backend && java -jar target\student-platform-1.0.0.jar" -ForegroundColor Yellow
    pause
    exit 1
}

Write-Host "[OK] 后端运行正常 (localhost:8080)" -ForegroundColor Green

# 启动 Tunnel
Write-Host "[INFO] 正在启动 Cloudflare Tunnel..." -ForegroundColor Cyan
Write-Host "[INFO] 按 Ctrl+C 停止" -ForegroundColor Gray
Write-Host ""

cloudflared tunnel run --config cloudflare/config.yml student-platform
