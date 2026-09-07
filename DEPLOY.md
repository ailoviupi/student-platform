# 🚀 免费部署操作指南

> 全栈项目一键上线：前端 → Vercel · 后端 → Render · 数据库 → PlanetScale · CI/CD → GitHub Actions

---

## 📋 前置准备

| 账号 | 地址 | 说明 |
|------|------|------|
| GitHub | https://github.com | 代码仓库 |
| Vercel | https://vercel.com | 前端托管 |
| Render | https://render.com | 后端托管 |
| PlanetScale | https://planetscale.com | MySQL 数据库 |

---

## 🗄️ 第一步：数据库（PlanetScale）

### 1.1 创建数据库

1. 登录 [PlanetScale](https://planetscale.com) → **Create a new database**
2. 名称：`snvec_student_platform`
3. 区域选择 **Singapore**（离国内最近）
4. 点击 **Create Database**

### 1.2 获取连接信息

1. 进入数据库 → 点击 **Connect** → 选择 **Java**
2. 记录以下信息：
   - **Host**: `aws.connect.psdb.cloud`
   - **Database**: `snvec_student_platform`
   - **Username**: `xxxxxxxx`
   - **Password**: `pscale_xxxxxxxx`

### 1.3 导入初始数据

```bash
# 方式一：通过 PlanetScale CLI
pscale shell snvec_student_platform main < sql/init_database.sql

# 方式二：通过 MySQL 直连（推荐）
mysql -h aws.connect.psdb.cloud -u <username> -p <database> < sql/init_database.sql
```

---

## 🔧 第二步：代码推送 GitHub

```bash
cd student-platform
git init
git add .
git commit -m "feat: 初始化项目 + 部署配置"

# 在 GitHub 创建仓库后
git remote add origin https://github.com/你的用户名/student-platform.git
git branch -M main
git push -u origin main
```

---

## 🌐 第三步：后端部署（Render）

### 3.1 创建 Web Service

1. 登录 [Render Dashboard](https://dashboard.render.com)
2. 点击 **New +** → **Web Service**
3. 连接 GitHub 仓库，选择 `student-platform`
4. 配置如下：

| 配置项 | 值 |
|--------|-----|
| **Name** | `student-platform-backend` |
| **Root Directory** | `backend` |
| **Runtime** | `Java` |
| **Build Command** | `mvn clean package -DskipTests` |
| **Start Command** | `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/student-platform-1.0.0.jar` |

### 3.2 环境变量设置

在 **Environment** 标签页添加：

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://aws.connect.psdb.cloud/snvec_student_platform?sslmode=VERIFY_IDENTITY&useUnicode=true&characterEncoding=utf-8` |
| `SPRING_DATASOURCE_USERNAME` | `<planetscale-username>` |
| `SPRING_DATASOURCE_PASSWORD` | `<planetscale-password>` |
| `JWT_SECRET` | `<随机生成32位字符串>` |
| `CORS_ALLOWED_ORIGINS` | `https://你的前端域名.vercel.app` |

> 💡 生成 JWT_SECRET：`openssl rand -base64 32`

### 3.3 获取 Render 服务地址

部署完成后，记录后端地址：`https://student-platform-backend.onrender.com`

---

## 🎨 第四步：前端部署（Vercel）

### 4.1 方式一：通过 Vercel Dashboard（推荐）

1. 登录 [Vercel](https://vercel.com) → **Add New** → **Project**
2. 导入 GitHub 仓库 `student-platform`
3. 配置如下：

| 配置项 | 值 |
|--------|-----|
| **Framework Preset** | `Vite` |
| **Root Directory** | `frontend` |
| **Build Command** | `npm run build` |
| **Output Directory** | `dist` |

4. 点击 **Deploy**，等待完成

### 4.2 方式二：通过 Vercel CLI

```bash
# 安装 CLI
npm i -g vercel

# 进入前端目录
cd frontend

# 部署
vercel --prod
```

### 4.3 更新 vercel.json 中的后端地址

修改 `frontend/vercel.json` 的 `destination` 为你的 Render 地址：

```json
{
  "rewrites": [
    { "source": "/api/(.*)", "destination": "https://student-platform-backend.onrender.com/api/$1" },
    { "source": "/(.*)", "destination": "/index.html" }
  ]
}
```

---

## 🔄 第五步：配置 CI/CD（GitHub Actions）

### 5.1 获取密钥

| 密钥 | 获取方式 |
|------|----------|
| `VERCEL_TOKEN` | Vercel → Settings → Tokens → Create |
| `VERCEL_ORG_ID` | Vercel → Settings → General → Team ID |
| `VERCEL_PROJECT_ID` | Vercel → 项目 → Settings → Project ID |
| `RENDER_API_KEY` | Render → Account Settings → API Keys |
| `RENDER_SERVICE_ID` | Render → 服务 → Settings → Service ID |

### 5.2 添加 GitHub Secrets

进入 GitHub 仓库 → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**

添加以下 5 个密钥：
- `VERCEL_TOKEN`
- `VERCEL_ORG_ID`
- `VERCEL_PROJECT_ID`
- `RENDER_API_KEY`
- `RENDER_SERVICE_ID`

### 5.3 测试自动部署

```bash
git add .
git commit -m "ci: 添加自动部署工作流"
git push origin main
```

推送后，GitHub Actions 会自动触发部署，可在 **Actions** 标签页查看进度。

---

## 🌍 第六步：绑定自定义域名（可选，免费）

### 6.1 在 Cloudflare 添加域名

1. 购买一个域名（或已有域名）
2. 将 DNS 服务器指向 Cloudflare
3. 在 Cloudflare 添加 CNAME 记录：

| Type | Name | Target |
|------|------|--------|
| CNAME | `www` | `cname.vercel-dns.com` |

### 6.2 Vercel 配置域名

1. Vercel → 项目 → **Settings** → **Domains**
2. 添加 `www.yourdomain.com`
3. 等待 DNS 生效（通常几分钟）

---

## ✅ 部署验证清单

部署完成后，按以下顺序检查：

- [ ] 访问前端地址，能正常打开登录页
- [ ] 使用默认账号登录（admin / admin123）
- [ ] 登录后能看到 Dashboard 仪表盘
- [ ] 各功能模块页面能正常加载
- [ ] 数据能正常增删改查

---

## ⚠️ 注意事项

### Render 免费 tier 限制
- **15 分钟无请求会休眠**，下次访问冷启动约 30 秒
- **每月 750 小时**运行时间，单实例够用

### PlanetScale 免费 tier 限制
- **5GB 存储**
- **10 亿行读取/月**
- **不支持外键、存储过程**（本项目无此依赖）

### 去除 Redis/RabbitMQ 影响
- 生产环境使用 `prod` profile 自动禁用
- 缓存退化为 JVM 内存缓存
- 消息队列相关功能暂时不可用

---

## 🔄 后续更新

```bash
# 修改代码后
git add .
git commit -m "feat: 更新描述"
git push origin main
# GitHub Actions 会自动触发重新部署
```

---

## 💰 费用预估

| 服务 | 免费额度 | 本项目用量 |
|------|----------|-----------|
| Vercel | 100GB 带宽/月 | < 5GB |
| Render | 750 小时/月 | ~720 小时 |
| PlanetScale | 5GB / 10亿行 | < 1GB |
| Cloudflare | 无限流量 | < 10GB |
| **总计** | **¥0** | **¥0** |

---

> 📌 **提示**：如果 Render 免费 tier 的休眠机制影响体验，可迁移到 Oracle Cloud 永久免费 VPS（4核ARM + 24GB内存），详见 `deploy-oracle.md`。
