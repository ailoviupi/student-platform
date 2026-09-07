# 一键启动脚本（本地 + Cloudflare Tunnel）
# 双击运行即可启动全部服务

@echo off
chcp 65001 >nul
title 学生工作平台 - 本地部署
color 0B

echo ================================================
echo    遂宁工程职业学院一站式学生工作智能平台
echo    本地部署 + Cloudflare Tunnel
echo ================================================
echo.

REM 检查 Java
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Java，请先安装 JDK 11+
    pause
    exit /b 1
)

REM 检查 Node
node -v >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Node.js，请先安装 Node.js 18+
    pause
    exit /b 1
)

REM 检查 cloudflared
cloudflared --version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 cloudflared，请先安装:
    echo   winget install Cloudflare.cloudflared
    pause
    exit /b 1
)

echo [1/4] 正在构建前端...
cd /d "%~dp0frontend"
call npm install
call npm build
echo [OK] 前端构建完成
echo.

echo [2/4] 正在复制前端到后端静态资源...
if not exist "..\backend\src\main\resources\static" mkdir "..\backend\src\main\resources\static"
xcopy /E /I /Y dist\* ..\backend\src\main\resources\static\
echo [OK] 前端文件已复制
echo.

echo [3/4] 正在构建后端...
cd /d "%~dp0backend"
call mvn clean package -DskipTests -q
echo [OK] 后端构建完成
echo.

echo [4/4] 正在启动后端服务...
start "后端服务" /min java -Dspring.profiles.active=prod -jar target\student-platform-1.0.0.jar
timeout /t 5 /nobreak >nul

echo [OK] 后端已启动 (localhost:8888)
echo.
echo ================================================
echo    正在启动 Cloudflare Tunnel...
echo    等待 Tunnel URL 出现后，即可在浏览器访问
echo ================================================
echo.

cloudflared tunnel --url http://localhost:8888

pause
