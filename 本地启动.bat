@echo off
chcp 65001 >nul
title 遂宁工程职业学院一站式学生工作智能平台 - 本地启动
color 0B

echo ================================================
echo    遂宁工程职业学院一站式学生工作智能平台
echo    校训: 厚德精技 · 求实创新
echo ================================================
echo.
echo   后端端口: 8888
echo   前端端口: 9999
echo ================================================
echo.

:: 检查 Java
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Java，请先安装 JDK 11 或更高版本
    pause
    exit /b 1
)

:: 检查 Node.js
node -v >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Node.js，请先安装 Node.js 18 或更高版本
    pause
    exit /b 1
)

:: 检查 MySQL
mysql --version >nul 2>&1
if errorlevel 1 (
    echo [警告] 未检测到 MySQL 命令行工具，请确保 MySQL 服务已启动
)

echo [1/3] 正在启动后端服务 (端口: 8888)...
start "后端服务 - 端口8888" cmd /c "cd /d %~dp0backend ^&^& java -jar target\student-platform-1.0.0.jar"

echo [2/3] 等待后端启动...
timeout /t 8 /nobreak >nul

echo [3/3] 正在启动前端服务 (端口: 9999)...
start "前端服务 - 端口9999" cmd /c "cd /d %~dp0frontend ^&^& npm run dev"

echo.
echo ================================================
echo    服务启动成功！
echo.
echo   前端地址: http://localhost:9999
echo   后端地址: http://localhost:8888/api
echo.
echo   默认账号: admin / admin123
echo.
echo   按任意键退出此窗口（服务将继续运行）...
echo ================================================
pause >nul
