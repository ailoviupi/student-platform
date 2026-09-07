# 🚀 免费部署 —— 超详细逐步指南

> 预计总耗时：**30-60 分钟**（大部分时间在等编译和部署）

---

## 📋 前置准备 —— 注册 4 个免费账号

### ① GitHub（代码仓库）

1. 打开 https://github.com
2. 点击 **Sign up** → 输入用户名 `ailoviupi`、邮箱 `2113267715@qq.com`、密码
3. 验证邮箱（收邮件点确认链接）
4. 登录后点击 **New repository**
5. 填写：
   - **Repository name**: `student-platform`
   - **Description**: 遂宁工程职业学院一站式学生工作智能平台
   - 选择 **Public**（公开，免费 CI/CD 需要）
   - **不要勾选** Add a README / .gitignore（因为你本地已有）
6. 点击 **Create repository**
7. 复制仓库地址：`https://github.com/ailoviupi/student-platform.git`

### ② Vercel（前端托管）

1. 打开 https://vercel.com
2. 点击 **Sign Up** → 选择 **Continue with GitHub**
3. 授权 Vercel 访问你的 GitHub 账号
4. 完成注册

### ③ Render（后端托管）

1. 打开 https://render.com
2. 点击 **Sign Up** → 选择 **GitHub** 登录
3. 授权 Render 访问你的 GitHub 账号
4. 完成注册

### ④ PlanetScale（MySQL 数据库）

1. 打开 https://planetscale.com
2. 点击 **Sign up** → 选择 **Sign up with GitHub**
3. 完成注册

---

## 🔗 第一步：推送代码到 GitHub

在本地已经完成了 git init 和 commit，现在推送到 GitHub：

```bash
cd D:\网页集\高校平台\student-platform
git remote add origin https://github.com/ailoviupi/student-platform.git
git branch -M main
git push -u origin main
```

> ⚠️ 如果提示输入用户名密码，输入你的 GitHub 用户名和 Personal Access Token（GitHub → Settings → Developer settings → Personal access tokens → Tokens classic → Generate new token）

---

## 🗄️ 第二步：创建数据库（PlanetScale）

### 2.1 创建数据库

1. 登录 https://planetscale.com
2. 点击右上角 **Create a new database**
3. 填写：
   - **Database name**: `snvec_student_platform`
   - **Cluster region**: 选择 **Singapore (Southeast Asia)**（离国内最近）
   - **Plan**: 选择 **Scalable (Free)**
4. 点击 **Create Database**
5. 等待约 1-2 分钟创建完成

### 2.2 获取连接信息

1. 进入刚创建的数据库
2. 点击右上角 **Connect** 按钮
3. 在弹出的窗口中选择 **Java**
4. 记录以下 4 个值（后面 Render 要用）：

```
Host:     aws.connect.psdb.cloud
Username: xxxxxxxxxxxxx
Password: pscale_xxxxxxxxxxxxxxxx
Database: snvec_student_platform
```

5. 点击 **Create Password** 生成密码（如果还没有的话）

### 2.3 导入初始数据

**方式一：使用 MySQL 命令行（推荐）**

```bash
# 在本地命令行执行
mysql -h aws.connect.psdb.cloud -u <你的username> -p <你的database> < D:\网页集\高校平台\student-platform\sql\init_database.sql
```

**方式二：使用 PlanetScale CLI**

```bash
# 安装 pscale CLI（如果未安装）
brew install planetscale/tap/pscale   # Mac
# 或
scoop install pscale                  # Windows

# 登录
pscale auth login

# 连接并导入
pscale shell snvec_student_platform main < D:\网页集\高校平台\student-platform\sql\init_database.sql
```

---

## 🔧 第三步：部署后端（Render）

### 3.1 创建 Web Service

1. 打开 https://dashboard.render.com
2. 点击右上角 **New +** → 选择 **Web Service**
3. 在 **Connect a GitHub repository** 页面：
   - 如果看不到 `student-platform`，点击 **Configure account** 授权 Render 访问仓库
   - 找到 `ailoviupi/student-platform`，点击 **Connect**
4. 填写配置：

| 配置项 | 填写内容 |
|--------|---------|
| **Name** | `student-platform-backend` |
| **Region** | `Singapore`（和数据库同区域） |
| **Branch** | `main` |
| **Root Directory** | `backend` |
| **Runtime** | `Java` |
| **Build Command** | `mvn clean package -DskipTests` |
| **Start Command** | `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/student-platform-1.0.0.jar` |
| **Instance Type** | `Free` |

5. 点击 **Advanced** 展开高级设置，添加环境变量：

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://aws.connect.psdb.cloud/snvec_student_platform?sslmode=VERIFY_IDENTITY&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai` |
| `SPRING_DATASOURCE_USERNAME` | `<你在 PlanetScale 复制的 Username>` |
| `SPRING_DATASOURCE_PASSWORD` | `<你在 PlanetScale 复制的 Password>` |
| `JWT_SECRET` | `<随机生成，见下方>` |

> 💡 **生成 JWT_SECRET**：在 PowerShell 运行 `[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 } | ForEach-Object { [byte]$_ }))`

6. 点击 **Create Web Service**
7. 等待部署完成（约 3-5 分钟，首次需要下载 Maven 依赖）

### 3.2 确认后端部署成功

1. 部署完成后，页面顶部会显示绿色 **Live** 标记
2. 记录后端 URL：`https://student-platform-backend.onrender.com`
3. 测试健康检查：浏览器打开 `https://student-platform-backend.onrender.com/api/actuator/health`
   - 应返回 JSON：`{"status":"UP",...}`

---

## 🎨 第四步：部署前端（Vercel）

### 4.1 导入项目

1. 打开 https://vercel.com/dashboard
2. 点击 **Add New...** → **Project**
3. 在 **Import Git Repository** 中找到 `ailoviupi/student-platform`
4. 点击 **Import**

### 4.2 配置项目

| 配置项 | 填写内容 |
|--------|---------|
| **Project Name** | `student-platform` |
| **Framework Preset** | `Vite` |
| **Root Directory** | `frontend` |
| **Build Command** | `npm run build` |
| **Output Directory** | `dist` |
| **Install Command** | `npm install` |

5. 点击 **Deploy**
6. 等待约 1-2 分钟完成部署

### 4.3 获取前端地址

部署完成后，Vercel 会分配一个默认域名：
- `https://student-platform-xxx.vercel.app`

记录这个地址，后面配置 CORS 要用。

### 4.4 更新 vercel.json 中的后端地址

修改 `frontend/vercel.json`，把 `student-platform-backend.onrender.com` 替换为你的实际 Render 地址：

```json
{
  "rewrites": [
    { "source": "/api/(.*)", "destination": "https://student-platform-backend.onrender.com/api/$1" },
    { "source": "/(.*)", "destination": "/index.html" }
  ],
  "headers": [
    {
      "source": "/assets/(.*)",
      "headers": [
        { "key": "Cache-Control", "value": "public, max-age=31536000, immutable" }
      ]
    }
  ]
}
```

然后提交并推送：

```bash
git add frontend/vercel.json
git commit -m "chore: 更新后端 API 地址"
git push origin main
```

Vercel 会自动重新部署。

### 4.5 更新 Render 的 CORS 配置

回到 Render Dashboard：
1. 进入 `student-platform-backend` 服务
2. 点击左侧 **Environment**
3. 找到 `CORS_ALLOWED_ORIGINS`，点击编辑
4. 更新为你的 Vercel 地址：`https://student-platform-xxx.vercel.app`
5. 点击 **Save**
6. Render 会自动重新部署

---

## 🔄 第五步：配置 CI/CD（GitHub Actions）

### 5.1 获取 Vercel 密钥

1. 打开 https://vercel.com/settings/tokens
2. 点击 **Create Token**
3. 名称：`github-actions`
4.  Scope：选择 **Full Account**
5. 点击 **Create**，复制 Token → 这就是 `VERCEL_TOKEN`

获取 ORG_ID 和 PROJECT_ID：
1. 打开 https://vercel.com → 进入你的项目
2. 点击 **Settings** → **General**
3. 在页面底部找到：
   - **Project ID** → 复制 → 这就是 `VERCEL_PROJECT_ID`
4. 打开 https://vercel.com/settings
5. 在 **General** 中找到 **Your ID** → 复制 → 这就是 `VERCEL_ORG_ID`

### 5.2 获取 Render 密钥

1. 打开 https://dashboard.render.com/user/settings
2. 滚动到 **API Keys** 部分
3. 点击 **Create API Key**
4. 名称：`github-actions`
5. 复制 API Key → 这就是 `RENDER_API_KEY`

获取 Service ID：
1. 打开 https://dashboard.render.com
2. 进入 `student-platform-backend` 服务
3. 点击左侧 **Settings**
4. 在页面 URL 中找到：`srv-xxxxxxxxxxxxxxxx`
   - 或滚动到 **Service ID** 字段
5. 复制 → 这就是 `RENDER_SERVICE_ID`

### 5.3 添加 GitHub Secrets

1. 打开 https://github.com/ailoviupi/student-platform/settings/secrets/actions
2. 点击 **New repository secret**
3. 依次添加以下 5 个密钥：

| Name | Secret |
|------|--------|
| `VERCEL_TOKEN` | <从 Vercel 复制的 Token> |
| `VERCEL_ORG_ID` | <从 Vercel 复制的 Org ID> |
| `VERCEL_PROJECT_ID` | <从 Vercel 复制的 Project ID> |
| `RENDER_API_KEY` | <从 Render 复制的 API Key> |
| `RENDER_SERVICE_ID` | <从 Render 复制的 Service ID> |

### 5.4 测试自动部署

```bash
git add .
git commit -m "ci: 配置 GitHub Actions 自动部署"
git push origin main
```

推送后：
1. 打开 https://github.com/ailoviupi/student-platform/actions
2. 可以看到正在运行的部署工作流
3. 等待两个 job 都变成绿色 ✅

---

## ✅ 第六步：验证部署

### 验证清单

- [ ] 浏览器打开 Vercel 前端地址，能看到登录页
- [ ] 使用默认账号 `admin` / `admin123` 登录
- [ ] 登录后跳转到 Dashboard 仪表盘
- [ ] 点击左侧菜单，各模块页面能正常加载
- [ ] 数据能正常显示和增删改查
- [ ] 打开 `https://student-platform-backend.onrender.com/api/actuator/health` 返回 `{"status":"UP"}`

---

## 🌍 第七步：绑定自定义域名（可选）

### 7.1 购买域名

- 国内：阿里云/腾讯云（约 ¥35/年）
- 国外：Namecheap/Cloudflare Registrar（约 $8/年）

### 7.2 Cloudflare 配置

1. 打开 https://cloudflare.com 注册账号
2. 点击 **Add a Site** → 输入你的域名
3. 选择 **Free** 计划
4. 按照提示将域名的 DNS 服务器改为 Cloudflare 提供的地址
5. 在 Cloudflare DNS 设置中添加记录：

| Type | Name | Content | Proxy |
|------|------|---------|-------|
| CNAME | `www` | `cname.vercel-dns.com` | Proxied |
| A | `@` | `76.76.21.21` | Proxied |

### 7.3 Vercel 配置域名

1. Vercel → 项目 → **Settings** → **Domains**
2. 添加 `www.yourdomain.com` 和 `yourdomain.com`
3. 等待 DNS 生效（通常几分钟到几小时）

---

## ⚠️ 常见问题

### Q: Render 显示 "Service is suspended"？
A: 免费 tier 每月 750 小时限制。如果超额，需要等下个月重置，或升级到 $7/月。

### Q: 前端访问后端返回 CORS 错误？
A: 检查 Render 的 `CORS_ALLOWED_ORIGINS` 环境变量是否正确设置为你的 Vercel 地址。

### Q: 数据库连接失败？
A: PlanetScale 需要 SSL。确认 `SPRING_DATASOURCE_URL` 包含 `sslmode=VERIFY_IDENTITY`。

### Q: 页面刷新后 404？
A: 确认 `vercel.json` 中的 rewrite 规则 `{ "source": "/(.*)", "destination": "/index.html" }` 存在。

### Q: Render 冷启动慢？
A: 免费 tier 15 分钟无请求会休眠，首次访问需要 30-60 秒冷启动。这是正常的。

---

## 💰 费用总结

| 服务 | 费用 | 说明 |
|------|------|------|
| GitHub | 免费 | 公开仓库无限使用 |
| Vercel | 免费 | 100GB 带宽/月 |
| Render | 免费 | 750 小时/月（单实例） |
| PlanetScale | 免费 | 5GB 存储 |
| Cloudflare | 免费 | 无限流量 |
| 域名 | 可选 | ¥35/年（不绑定也能用） |
| **总计** | **¥0** | — |

---

## 🔄 后续更新代码

```bash
# 修改代码后
git add .
git commit -m "feat: 描述你的修改"
git push origin main
# GitHub Actions 自动部署到 Vercel + Render
```

---

> 📌 **提示**：如果 Render 免费 tier 的休眠机制影响体验，后续可迁移到 Oracle Cloud 永久免费 VPS（4核ARM + 24GB内存）。
