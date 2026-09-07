# 🌍 Oracle Cloud 永久免费 VPS 部署指南

> 无需信用卡，4 核 ARM + 24GB 内存 + 200GB 存储，永久免费

---

## 📋 免费资源

| 资源 | 免费额度 |
|------|----------|
| CPU | 4 核 ARM (Ampere A1) |
| 内存 | 24 GB |
| 存储 | 200 GB |
| 带宽 | 10 TB/月 |
| 实例数 | 最多 4 个 |

---

## 🔧 第一步：注册 Oracle Cloud

1. 打开 https://cloud.oracle.com
2. 点击 **Sign Up**
3. 填写邮箱、密码、用户名
4. 选择 **Home Region** 为 **Singapore**（离国内最近）
5. 验证邮箱
6. 填写个人信息（地址可填国内真实地址）
7. **不需要信用卡**（选择 Pay-As-You-Go 也不会扣费，免费额度足够）
8. 完成注册并登录

---

## 🔧 第二步：创建 ARM 实例

1. 登录 Oracle Cloud Console
2. 点击左上角菜单 → **Compute** → **Instances**
3. 点击 **Create Instance**
4. 配置如下：

### Image and Shape
- **Image**: 选择 **Canonical Ubuntu 22.04**
- **Shape**: 选择 **VM.Standard.A1.Flex**
  - **Number of OCPUs**: 4
  - **Memory (GB)**: 24

### Networking
- **Virtual cloud network**: 创建新的 `vcn-1`
- **Subnet**: 创建新的公共子网
- **Assign a public IPv4 address**: ✅ 勾选

### Add SSH keys
- 选择 **Generate a key pair for me**
- 点击 **Save Private Key** 保存私钥到本地
- 点击 **Save Public Key** 保存公钥到本地

5. 点击 **Create**
6. 等待 1-2 分钟，实例状态变为 **Running**
7. 记录 **Public IP Address**（如 `152.67.xxx.xxx`）

---

## 🔧 第三步：配置网络安全

1. 进入实例详情页 → 点击 **Subnet** 链接
2. 点击 **Default Security List** 或 **Security List**
3. 点击 **Add Ingress Rules**，添加以下规则：

| Source CIDR | Protocol | Port | Description |
|-------------|----------|------|-------------|
| `0.0.0.0/0` | TCP | 80 | HTTP |
| `0.0.0.0/0` | TCP | 443 | HTTPS |
| `0.0.0.0/0` | TCP | 22 | SSH |

4. 点击 **Add Ingress Rules** 保存

---

## 🔧 第四步：连接实例

### 使用 SSH 连接

```bash
# 修改私钥权限（Windows 用 Git Bash 或 WSL）
chmod 600 ~/Downloads/ssh-key-2024-xx-xx.key

# 连接
ssh -i ~/Downloads/ssh-key-2024-xx-xx.key ubuntu@<你的公网IP>
```

### 使用 PowerShell

```powershell
ssh -i $env:USERPROFILE\Downloads\ssh-key-2024-xx-xx.key ubuntu@<你的公网IP>
```

> ⚠️ 如果连接超时，检查安全组是否开放了 22 端口

---

## 🔧 第五步：安装 Docker

连接实例后，执行以下命令：

```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装 Docker
curl -fsSL https://get.docker.com | sh

# 将当前用户加入 docker 组
sudo usermod -aG docker $USER
newgrp docker

# 安装 Docker Compose
sudo apt install -y docker-compose-plugin

# 验证安装
docker --version
docker compose version
```

---

## 🔧 第六步：上传项目代码

### 方式一：Git Clone（推荐）

```bash
# 安装 Git
sudo apt install -y git

# 克隆项目
git clone https://github.com/ailoviupi/student-platform.git
cd student-platform
```

### 方式二：SCP 上传

```bash
# 在本地 PowerShell 执行
scp -i $env:USERPROFILE\Downloads\ssh-key-2024-xx-xx.key -r D:\网页集\高校平台\student-platform\* ubuntu@<你的公网IP>:/home/ubuntu/student-platform/
```

---

## 🔧 第七步：配置环境变量

```bash
cd ~/student-platform

# 创建环境变量文件
cat > .env << 'EOF'
# 数据库配置 (TiDB Cloud)
DB_HOST=gateway01.ap-northeast-1.prod.aws.tidbcloud.com
DB_PORT=4000
DB_NAME=snvec_student_platform
DB_USERNAME=fDqMarNRMypfEJb.root
DB_PASSWORD=kUjjc9sM6IrXgfm3

# JWT 密钥（随机生成，不要用这个默认值）
JWT_SECRET=请替换为随机字符串

# 前端 API 地址
VITE_API_BASE=/api
EOF

# 生成随机 JWT_SECRET
sed -i "s/请替换为随机字符串/$(openssl rand -base64 32)/" .env

echo "✅ 环境变量配置完成"
cat .env
```

---

## 🔧 第八步：构建并启动

```bash
cd ~/student-platform

# 构建并启动所有服务
docker compose -f docker-compose.prod.yml up -d --build

# 查看日志
docker compose -f docker-compose.prod.yml logs -f

# 检查状态
docker compose -f docker-compose.prod.yml ps
```

> ⏱️ 首次构建约 3-5 分钟（下载 Maven 依赖）

---

## 🔧 第九步：验证部署

### 检查服务状态

```bash
docker compose -f docker-compose.prod.yml ps
```

应看到两个服务都是 `Up` 状态。

### 测试 API

```bash
# 健康检查
curl http://localhost:80/api/actuator/health

# 应返回 {"status":"UP",...}
```

### 浏览器访问

打开浏览器访问：
```
http://<你的公网IP>
```

应能看到登录页面。

---

## 🔧 第十步：配置域名（可选）

### 免费域名

1. 打开 https://www.freenom.com 注册免费域名（如 `snvec-platform.tk`）
2. 或使用 Cloudflare 添加已有域名

### Cloudflare 配置

1. 打开 https://cloudflare.com 注册账号
2. 添加域名，将 DNS 服务器指向 Cloudflare
3. 在 Cloudflare DNS 添加 A 记录：

| Type | Name | Content | Proxy |
|------|------|---------|-------|
| A | `@` | `<你的公网IP>` | DNS only |
| A | `www` | `<你的公网IP>` | DNS only |

4. 等待 DNS 生效（几分钟到几小时）

### 配置 SSL（Let's Encrypt）

```bash
# 安装 certbot
sudo apt install -y certbot

# 获取证书（替换为你的域名）
sudo certbot certonly --standalone -d snvec-platform.tk -d www.snvec-platform.tk

# 将证书复制到项目目录
sudo cp /etc/letsencrypt/live/snvec-platform.tk/fullchain.pem docker/ssl/
sudo cp /etc/letsencrypt/live/snvec-platform.tk/privkey.pem docker/ssl/

# 更新 nginx 配置启用 HTTPS
# 修改 docker-compose.prod.yml 挂载 SSL 目录
# 重启服务
docker compose -f docker-compose.prod.yml restart frontend
```

---

## ⚠️ 常见问题

### Q: 无法连接实例？
A: 检查安全组是否开放了 22 端口（SSH）、80 端口（HTTP）。

### Q: Docker 构建失败？
A: 检查内存是否充足：`free -h`。ARM 实例首次构建可能较慢。

### Q: 无法连接 TiDB Cloud？
A: 检查 `.env` 中的数据库配置是否正确，特别是密码是否有特殊字符。

### Q: 页面白屏？
A: 查看前端日志：`docker compose -f docker-compose.prod.yml logs frontend`

### Q: API 返回 502？
A: 查看后端日志：`docker compose -f docker-compose.prod.yml logs backend`

---

## 🔄 更新代码

```bash
cd ~/student-platform

# 拉取最新代码
git pull

# 重新构建并启动
docker compose -f docker-compose.prod.yml up -d --build

# 查看日志
docker compose -f docker-compose.prod.yml logs -f
```

---

## 📊 监控命令

```bash
# 查看容器状态
docker compose -f docker-compose.prod.yml ps

# 查看资源使用
docker stats

# 查看日志
docker compose -f docker-compose.prod.yml logs -f --tail=100

# 重启服务
docker compose -f docker-compose.prod.yml restart

# 停止服务
docker compose -f docker-compose.prod.yml down
```

---

## 💰 费用

| 项目 | 费用 |
|------|------|
| Oracle Cloud ARM 实例 | 永久免费 |
| TiDB Cloud | 免费 5GB |
| 域名 | 免费（Freenom）或 ¥35/年 |
| Cloudflare CDN | 免费 |
| **总计** | **¥0** |

---

> 📌 **提示**：Oracle Cloud 免费实例只要每 6 个月登录一次控制台就不会回收。建议设置日历提醒。
