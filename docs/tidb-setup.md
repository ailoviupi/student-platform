# 🗄️ TiDB Cloud 数据库设置指南

> TiDB Cloud 是 PingCAP（中国公司）提供的 MySQL 兼容数据库，国内可直接访问，有免费额度。

---

## 📋 TiDB Cloud 免费额度

| 资源 | 免费额度 |
|------|----------|
| 存储 | 5 GB |
| 读写请求 | 5000 万 CU/月 |
| 连接数 | 5 个 |
| 集群数 | 1 个 |

---

## 🔧 第一步：注册 TiDB Cloud

1. 打开 https://tidbcloud.com
2. 点击 **Sign up** → 选择 **Sign up with GitHub** 或 **Email**
3. 完成注册（不需要信用卡）
4. 登录后进入 TiDB Cloud Console

---

## 🔧 第二步：创建集群

1. 在 TiDB Cloud Console 点击 **Create Cluster**
2. 选择 **Serverless**（免费计划）
3. 配置：
   - **Cluster name**: `snvec-platform`
   - **Region**: 选择 **Singapore**（离国内最近）
   - **Password**: 设置一个密码并记住（如 `Snvec2024@tidb`）
4. 点击 **Create**
5. 等待约 1-2 分钟创建完成

---

## 🔧 第三步：获取连接信息

1. 创建完成后，点击集群名称进入详情页
2. 点击右上角 **Connect** 按钮
3. 在弹出的窗口中选择 **General** 连接
4. 记录以下信息：

```
Endpoint:  xxxxxx.tidbcloud.com
Port:      4000
Username:  root（或你创建的用户）
Password:  你设置的密码
Database:  test（默认）
```

5. 点击 **Generate Password** 生成临时密码（如果还没有的话）

---

## 🔧 第四步：导入初始数据

### 方式一：使用 MySQL 命令行（推荐）

```bash
mysql -h <Endpoint> -P 4000 -u <Username> -p < D:\网页集\高校平台\student-platform\sql\init_database.sql
```

### 方式二：使用 TiDB Cloud 控制台

1. 在集群详情页点击 **Console**（或 **Chat2Query**）
2. 输入 SQL 命令创建数据库：
   ```sql
   CREATE DATABASE IF NOT EXISTS snvec_student_platform;
   ```
3. 切换到该数据库并粘贴 `sql/init_database.sql` 中的内容执行

### 方式三：使用 DBeaver/Navicat 等工具

1. 打开 DBeaver/Navicat
2. 新建 MySQL 连接：
   - Host: `<Endpoint>`
   - Port: `4000`
   - Username: `<Username>`
   - Password: `<Password>`
   - Database: `test`
3. 连接后创建数据库 `snvec_student_platform`
4. 执行 `sql/init_database.sql`

---

## 🔧 第五步：更新后端配置

### 更新 `application-prod.yml`

将 TiDB Cloud 的连接信息替换：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${SPRING_DATASOURCE_URL:jdbc:mysql://<Endpoint>:4000/snvec_student_platform?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true}
    username: ${SPRING_DATASOURCE_USERNAME:root}
    password: ${SPRING_DATASOURCE_PASSWORD:你的密码}
```

### 更新 Render 环境变量

在 Render Dashboard → 服务 → Environment 中添加：

| Key | Value |
|-----|-------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://xxxxxx.tidbcloud.com:4000/snvec_student_platform?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=Asia/Shanghai` |
| `SPRING_DATASOURCE_USERNAME` | `<你的用户名>` |
| `SPRING_DATASOURCE_PASSWORD` | `<你的密码>` |

---

## ⚠️ TiDB Cloud 注意事项

1. **TiDB 兼容 MySQL 8.0**，但部分特性略有不同：
   - 支持外键语法但不强制约束（和 PlanetScale 一样）
   - 存储引擎默认是 TiKV（自动分布式）
   - 本项目所有 SQL 语句完全兼容

2. **连接需要 SSL**：URL 中必须包含 `useSSL=true`

3. **免费集群自动暂停**：1 小时无连接会自动暂停，下次连接自动恢复（类似 Render 的休眠）

4. **中国大陆可直接访问**，无需科学上网

---

## ✅ 完成标志

执行以下命令验证连接：

```bash
mysql -h <Endpoint> -P 4000 -u <Username> -p snvec_student_platform -e "SHOW TABLES;"
```

应能看到 `sm_` 开头的表列表。
