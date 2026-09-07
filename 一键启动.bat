@echo off
chcp 65001 >nul
title 遂宁工程职业学院一站式学生工作智能平台 - 一键启动
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

:menu
echo  [1] 启动全部服务 (后端 + 前端)
echo  [2] 仅启动后端服务 (端口: 8888)
echo  [3] 仅启动前端服务 (端口: 9999)
echo  [4] 编译打包后端
echo  [5] 安装前端依赖
echo  [0] 退出
echo.
set /p choice=请选择操作 [0-5]: 

if "%choice%"=="1" goto startAll
if "%choice%"=="2" goto startBackend
if "%choice%"=="3" goto startFrontend
if "%choice%"=="4" goto buildBackend
if "%choice%"=="5" goto installFrontend
if "%choice%"=="0" goto end
echo 无效选择，请重新输入
timeout /t 2 /nobreak >nul
cls
goto menu

:startAll
echo.
echo [1/3] 正在检查环境...
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Java，请先安装 JDK
    pause
    goto menu
)
node -v >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Node.js，请先安装 Node.js
    pause
    goto menu
)

echo [2/3] 正在启动后端服务 (端口: 8888)...
start "后端服务" cmd /c "cd /d %~dp0backend ^&^& java -jar target\student-platform-1.0.0.jar"
timeout /t 8 /nobreak >nul

echo [3/3] 正在启动前端服务 (端口: 9999)...
start "前端服务" cmd /c "cd /d %~dp0frontend ^&^& npm run dev"

echo.
echo ================================================
echo    服务启动成功！
echo.
echo   前端地址: http://localhost:9999
echo   后端地址: http://localhost:8888/api
echo.
echo   默认账号: admin / admin123
echo ================================================
echo.
echo 按任意键返回菜单...
pause >nul
cls
goto menu

:startBackend
echo.
echo 正在启动后端服务 (端口: 8888)...
cd /d "%~dp0backend"
if not exist "target\student-platform-1.0.0.jar" (
    echo 未检测到编译包，正在编译...
    call mvn clean package -DskipTests
)
start "后端服务" cmd /c "cd /d %~dp0backend ^&^& java -jar target\student-platform-1.0.0.jar"
echo [OK] 后端服务已启动，访问 http://localhost:8888/api
echo.
echo 按任意键返回菜单...
pause >nul
cls
goto menu

:startFrontend
echo.
echo 正在启动前端服务 (端口: 9999)...
cd /d "%~dp0frontend"
if not exist "node_modules" (
    echo 未检测到依赖，正在安装...
    call npm install
)
start "前端服务" cmd /c "cd /d %~dp0frontend ^&^& npm run dev"
echo [OK] 前端服务已启动，访问 http://localhost:9999
echo.
echo 按任意键返回菜单...
pause >nul
cls
goto menu

:buildBackend
echo.
echo 正在编译打包后端...
cd /d "%~dp0backend"
call mvn clean package -DskipTests
if errorlevel 1 (
    echo [错误] 编译失败
) else (
    echo [OK] 编译成功，包位置: target\student-platform-1.0.0.jar
)
echo.
echo 按任意键返回菜单...
pause >nul
cls
goto menu

:installFrontend
echo.
echo 正在安装前端依赖...
cd /d "%~dp0frontend"
call npm install
if errorlevel 1 (
    echo [错误] 安装失败
) else (
    echo [OK] 前端依赖安装成功
)
echo.
echo 按任意键返回菜单...
pause >nul
cls
goto menu

:end
echo.
echo 感谢使用遂宁工程职业学院一站式学生工作智能平台！
timeout /t 2 /nobreak >nul
exit
