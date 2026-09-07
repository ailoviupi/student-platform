# 🚀 Cloudflare Tunnel 本地部署指南

> 零成本，国内直接访问，无需信用卡，无需公网 IP

---

## 📋 架构

```
用户 → Cloudflare CDN → Cloudflare Tunnel → 本地电脑 (Spring Boot + 前端)
                                                              ↓
                                                         TiDB Cloud
```

---

## 🔧 第一步：安装 cloudflared

### 方式一：winget（推荐）

```powershell
winget install Cloudflare.cloudflared
```

### 方式二：手动下载

1. 打开 https://github.com/cloudflare/cloudflared/releases
2. 下载 `cloudflared-windows-amd64.exe`
3. 重命名为 `cloudflared.exe`
4. 放到 `C:\Windows\System32\` 或项目 `cloudflare/` 目录

### 验证安装

```powershell
cloudflared --version
```

---

## 🔧 第二步：创建 Cloudflare Tunnel

### 方式一：命令行创建（推荐）

```powershell
# 登录 Cloudflare（会打开浏览器授权）
cloudflared tunnel login

# 创建 Tunnel
cloudflared tunnel create student-platform

# 这会生成一个 JSON 凭证文件，记录它的位置
# 通常在: C:\Users\<用户名>\.cloudflared\student-platform.json
```

### 方式二：使用 trycloudflare.com 临时域名

```powershell
# 获取一个随机临时域名
cloudflared tunnel --url http://localhost:8080

# 输出类似: https://xxxxx-xxxxx-xxxxx.trycloudflare.com
# 这个域名可以直接访问，但每次重启会变
```

---

## 🔧 第三步：配置 Tunnel

### 使用固定域名（需要自己的域名）

如果你有域名（如 `snvec-platform.com`），在 Cloudflare DNS 中添加 CNAME 记录：

| Type | Name | Target |
|------|------|--------|
| CNAME | `app` | `<tunnel-id>.cfargotunnel.com` |

然后编辑 `cloudflare/config.yml`：

```yaml
tunnel: student-platform
credentials-file: C:\Users\<用户名>\.cloudflared\student-platform.json

ingress:
  - hostname: app.snvec-platform.com
    service: http://localhost:8080
  - service: http_status:404
```

### 使用临时域名（快速测试）

直接运行：

```powershell
cloudflared tunnel --url http://localhost:8080
```

---

## 🔧 第四步：启动服务

### 1. 确保 TiDB Cloud 数据库已就绪

数据库已创建并导入数据（之前已完成）。

### 2. 启动 Spring Boot 后端

```powershell
cd D:\网页集\高校平台\student-platform\backend
java -Dspring.profiles.active=prod -jar target\student-platform-1.0.0.jar
```

### 3. 构建前端（首次）

```powershell
cd D:\网页集\高校平台\student-platform\frontend
npm install
npm run build
```

### 4. 将前端构建产物放到后端 resources

Spring Boot 会自动托管 `frontend/dist/` 目录的静态文件。

修改 `application-prod.yml` 添加静态资源路径：

```yaml
spring:
  web:
    resources:
      static-locations: classpath:/static/,file:./frontend/dist/
```

将 `frontend/dist/` 内容复制到 `backend/src/main/resources/static/`：

```powershell
xcopy /E /I /Y D:\网页集\高校平台\student-platform\frontend\dist\* D:\网页集\高校平台\student-platform\backend\src\main\resources\static\
```

### 5. 启动 Cloudflare Tunnel

```powershell
cd D:\网页集\高校平台\student-platform
cloudflared tunnel --url http://localhost:8080
```

### 6. 访问

浏览器打开 Cloudflare 提供的 URL（如 `https://xxxxx.trycloudflare.com`）

---

## 🔧 第五步：配置域名（可选）

### 免费域名

1. 打开 https://www.freenom.com
2. 搜索免费域名（如 `snvec-platform.tk`）
3. 注册并购买（选择 12 个月免费）
4. 在 Cloudflare 添加域名
5. 将域名 DNS 服务器指向 Cloudflare

### Cloudflare DNS 配置

| Type | Name | Content |
|------|------|---------|
| CNAME | `app` | `<tunnel-id>.cfargotunnel.com` |

### 更新 config.yml

```yaml
tunnel: student-platform
credentials-file: C:\Users\<用户名>\.cloudflared\student-platform.json

ingress:
  - hostname: app.snvec-platform.tk
    service: http://localhost:8080
  - service: http_status:404
```

---

## ⚠️ 注意事项

### 本地电脑需要一直开机
Cloudflare Tunnel 需要本地电脑运行才能访问。如果电脑关机，服务将不可用。

### 临时域名每次会变
使用 `--url` 方式获取的临时域名每次重启都会变化。如需固定域名，需配置自己的域名。

### 端口确保可用
确保 Spring Boot 运行在 8080 端口（或修改 config.yml 中的端口）。

### 防火墙
确保 Windows 防火墙允许 cloudflared.exe 和 Java 访问网络。

---

## 🔄 更新代码

```powershell
# 1. 拉取最新代码
cd D:\网页集\高校平台\student-platform
git pull

# 2. 重新构建后端
cd backend
mvn clean package -DskipTests

# 3. 重新构建前端
cd ..\frontend
npm run build

# 4. 复制前端到后端静态资源
xcopy /E /I /Y dist\* ..\backend\src\main\resources\static\

# 5. 重启后端
cd ..\backend
java -Dspring.profiles.active=prod -jar target\student-platform-1.0.0.jar

# 6. 重启 Tunnel（如果用了固定域名）
cloudflared tunnel run --config cloudflare/config.yml student-platform
```

---

## 💡 进阶：使用 cloudflared service 服务（后台运行）

```powershell
# 安装为 Windows 服务
cloudflared service install

# 启动服务
net start cloudflared

# 停止服务
net stop cloudflared
```

---

## 💰 费用

| 项目 | 费用 |
|------|------|
| Cloudflare Tunnel | 免费 |
| Cloudflare CDN | 免费 |
| TiDB Cloud | 免费 5GB |
| 域名 | 免费（Freenom）或 ¥35/年 |
| **总计** | **¥0** |

---

> 📌 **提示**：适合演示和个人使用。如果需要 24/7 在线，建议后续迁移到 Oracle Cloud 或国内云服务器。
