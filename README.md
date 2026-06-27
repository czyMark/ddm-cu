# 代代米日淘 · 日本海淘代购小程序

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

一套完整的 **日本海淘 / 日淘代购** 全栈解决方案，面向中国用户提供雅虎拍卖（Yahoo Auctions）与煤炉（Mercari）代购服务。本仓库包含 **用户端微信小程序**、**运营管理后台**、**后端 API 服务**，以及仓库出入库 APP 的工程骨架。

> 后端为 Spring Boot Web 层（Controller），依赖 `demo-service-logic` 业务模块（需自行补充或联系作者获取）。

---

## 项目简介

**代代米日淘** 是一个成熟的日淘代购业务系统，覆盖从商品浏览、下单支付、日本仓入库、合并发货到国内签收的全链路。

**商业模式：**

- 用户在小程序中搜索、浏览日本电商平台商品
- 平台代用户在日本下单或参与竞拍，商品先进入日本仓库
- 用户可合并多个订单，选择邮寄路线，支付国际运费等尾款后发往国内
- 通过中日汇率换算、代购手续费、竞拍保证金、仓储/包装/国际运费等实现盈利
- 支持邀请码与下级经销分销体系

**支持平台：**

| 平台 | 模式 | 说明 |
|------|------|------|
| **雅虎拍卖（Yahoo Auctions）** | 竞拍代购 | 预约出价 / 即时出价 / 即决价，需缴纳保证金 |
| **煤炉（Mercari）** | 一口价直购 | 支持购物车批量下单 |

---

## 仓库结构

```
ddmGit/
├── 前台小程序/              # 用户端微信小程序（核心业务）
├── 后台管理/                # 运营管理 Web 后台
├── 后端服务/                # Spring Boot 后端 API（Controller 层）
├── 代代米日淘出入库PC/       # PC 端仓库出入库客户端（WinForms + .NET）
├── 代代米日淘出入库APP/      # 仓库出入库 APP（工程骨架，待完善）
├── database/                # 数据库初始化脚本
│   ├── yahoo.sql              # 主业务库
│   └── recpiptuserdb.sql      # PC 出入库本地库
├── docs/                    # 文档与资源
│   └── wechat-qrcode.png    # 作者微信二维码
├── README.md
└── README.en.md
```

### 前台小程序

面向 C 端用户的微信小程序，基于 **uni-app + Vue 2** 开发，编译目标为微信小程序。

**TabBar：** 首页 · 客服 · 收藏 · 我的

**核心模块：**

| 模块 | 功能 |
|------|------|
| 首页 | 轮播图、平台入口（雅虎/煤炉）、分类导航、公告 |
| 雅虎代购 | 分类浏览、搜索、商品详情、竞拍出价、出价记录 |
| 煤炉代购 | 分类浏览、搜索、商品详情、购物车、申请发货 |
| 个人中心 | 登录、订单、钱包、地址、收藏、足迹、充值、保证金 |
| 订单管理 | 竞拍订单（预约/进行中/得标/失败）、物流订单（待支付→已发货） |

### 后台管理

面向运营/客服/仓库管理人员的 Web 管理端，基于 **Vue 2 + Element UI** 开发。

**核心模块：**

| 模块 | 功能 |
|------|------|
| 订单管理 | 雅虎/煤炉订单统一管理，15+ 状态筛选，费用核算，发货，Excel 导出 |
| 用户管理 | 用户信息、备注、设管理员、提现审核、押金解冻 |
| 财务管理 | 用户提现、竞拍保证金审批、下级经销提成 |
| 内容管理 | 轮播图、公告、常见问题、用户协议、收费标准 |
| 系统设置 | 客服联系方式、汇率维护、雅虎/煤炉 Cookie 配置、商品分类 |

### 代代米日淘出入库 PC

面向仓库操作人员的 **Windows 桌面客户端**，基于 **.NET Framework 4.7.2 + WinForms** 开发，支持扫码枪入库/出库。

**功能：**
- 扫码入库：扫描商品条码，同步更新雅虎/煤炉订单为「已入库」
- 扫码出库：记录包裹信息，调用后端 API 标记发货
- 入库/出库记录查询与分页
- 对接后端 API（admin 登录 + 订单状态更新）
- 本地 MySQL 数据库 `recpiptuserdb` 记录扫描日志

**运行：** 使用 Visual Studio 打开 `代代米日淘出入库PC/WinReceipt/WinReceipt.sln`，编译运行。

### 代代米日淘出入库 APP

基于 uni-app 的仓库端 APP 工程骨架，**目前尚未实现完整业务**。当前仓库操作通过后台管理的订单列表完成（标记入库、计算费用、发货等）。

### 后端服务

基于 **Spring Boot + MyBatis + Druid + Redis** 的 REST API 服务，提供小程序与后台管理所需的全部接口。

**Controller 模块（24 个）：**

| 模块 | 接口前缀 | 功能 |
|------|----------|------|
| 用户 | `/user` | 微信登录、手机号登录、地址、资料、邀请码 |
| 雅虎订单 | `/yahooOrder` | 竞拍下单、补差价、合并发货、订单查询 |
| 煤炉订单 | `/MerOrder` | 直购下单、购物车结算、尾款、合并发货 |
| 商品 | `/good`、`/Mercarigood` | 雅虎/煤炉商品搜索、详情、分类 |
| 购物车 | `/MrShppingCar` | 煤炉购物车 CRUD |
| 支付 | `/pay` | 微信支付统一下单与回调 |
| 账户 | `/account` | 余额充值、保证金、变更记录 |
| 管理 | `/admin` | 后台登录、员工管理 |
| 内容 | `/image`、`/notice`、`/question`、`/setting` | CMS 内容管理 |
| 其他 | `/rate`、`/fenlei`、`/collect`、`/phone`、`/upload` 等 | 汇率、分类、收藏、短信、上传 |

> **注意：** 当前后端目录为 `demo-portal-web`（Web 层），Maven 依赖 `demo-service-logic` 模块（Service/DAO/Entity 等业务逻辑）。该模块源码不在本仓库中，需联系作者获取或自行实现。

---

## 业务流程

### 雅虎竞拍订单

```
预约出价中 → 最高出价中 → 竞拍成功(补差价) / 竞拍失败 / 被超越
→ 付款成功待入库 → 已入库 → 申请发货 → 待付尾款 → 待发货 → 已发货
```

### 煤炉直购订单

```
付款成功待入库 → 已入库 → 申请发货 → 待付尾款 → 待发货 → 已发货
```

### 完整链路

```
用户搜索商品 → 雅虎竞拍 / 煤炉直购 → 微信支付首笔
→ 日本代购下单 → 商品到仓（已入库）
→ 用户合并订单申请发货 → 后台计算国际运费
→ 用户付尾款 → 后台发货 → 用户签收
```

**支付方式：** 全部通过微信支付（商品下单、余额充值、保证金充值、补差价、国际运费尾款等）。

---

## 技术栈

| 子项目 | 框架 | UI 库 / 中间件 | 说明 |
|--------|------|----------------|------|
| 前台小程序 | uni-app + Vue 2 | uni-ui | 编译为微信小程序 |
| 后台管理 | Vue 2 + Vue CLI 5 | Element UI 2 | Web 管理端 |
| 后端服务 | Spring Boot + MyBatis | Druid、Redis、Swagger | REST API |
| 出入库 PC | .NET Framework 4.7.2 | WinForms | 仓库扫码入库/出库 |
| 出入库 APP | uni-app + Vue 2 | uni-ui | 待完善 |

---

## 快速开始

### 环境要求

- Node.js 14+
- JDK 8+
- Maven 3.6+
- MySQL 5.7+
- Redis
- HBuilderX（用于 uni-app 小程序开发）
- 微信开发者工具

### 前台小程序

1. 使用 HBuilderX 打开 `前台小程序/` 目录
2. 修改 `manifest.json` 中的微信小程序 AppID
3. 修改 `utils/url.js` 中的 API 基址
4. 运行 → 运行到小程序模拟器 → 微信开发者工具

### 后台管理

```bash
cd 后台管理
yarn install
yarn serve    # 开发模式，默认端口 8080
yarn build    # 生产构建
```

开发模式下 API 请求会代理到 `https://ddm-cu.com`，部署前请修改 `vue.config.js` 和 `src/http/api.js` 中的接口地址。

### 后端服务

1. 配置 MySQL 数据库（库名 `yahoo`）和 Redis
2. 修改 `后端服务/src/main/resources/application-druid.yml` 中的数据库连接
3. 修改 `application.yml` 中的 Redis、邮件、SSL 等配置
4. 在 `UserController.java`、`PayController.java` 中填入微信小程序 AppID/Secret 和支付密钥
5. 确保 `demo-service-logic` 依赖模块可用（联系作者获取或自行实现）
6. 使用 Maven 编译运行：

```bash
cd 后端服务
mvn clean package
java -jar target/demo-portal-web-1.0-SNAPSHOT.jar
```

默认端口 443（HTTPS），开发环境可在 `application.yml` 中改为 8080 并关闭 SSL。

### 数据库

```bash
mysql -u root -p < database/yahoo.sql
mysql -u root -p < database/recpiptuserdb.sql
```

详见 [database/README.md](database/README.md)。

### 出入库 PC 客户端

1. 导入 `database/recpiptuserdb.sql`
2. 修改 `代代米日淘出入库PC/WinReceipt/ReceiptDAL/SqlHelper.cs` 数据库连接
3. 使用 Visual Studio 打开 `WinReceipt.sln` 编译运行

---

## 后端 API 说明

后端 Controller 层已包含在本仓库 `后端服务/` 目录，提供以下核心能力：

- 用户认证（微信 OAuth + 短信验证码）
- 雅虎/煤炉商品数据抓取或 API 代理（需 Cookie 维护）
- 订单状态机与费用计算
- 微信支付统一下单与回调
- 文件上传、内容管理等

API 接口定义参考：

- 前台小程序：`前台小程序/utils/api.js`
- 后台管理：`后台管理/src/http/api.js`
- 后端 Controller：`后端服务/src/main/java/com/zhwl/demo/controller/`

默认 API 域名（部署时需替换）：

| 域名 | 用途 |
|------|------|
| `https://ddm-cu.com` | 主业务 API |
| `https://fenlei.ddm-cu.com` | 内容/分类 API |

---

## 部署注意事项

1. **替换微信小程序 AppID** — `前台小程序/manifest.json`、`后端服务/.../UserController.java`
2. **替换 API 基址** — `前台小程序/utils/url.js`、`后台管理/vue.config.js`
3. **配置后端** — 数据库、Redis、微信支付、短信服务（见 `后端服务/src/main/resources/`）
4. **补充 demo-service-logic 模块** — 后端业务逻辑层，联系作者获取
5. **清理测试数据** — 部署前请检查硬编码信息

---

## 联系作者 · 二次开发 / 商务合作

本项目已开源，欢迎 Star、Fork 和学习参考。

如需 **二次开发、私有化部署、技术咨询或商务合作**，请联系作者：

| 联系方式 | 信息 |
|----------|------|
| **手机号** | 185151626295 |
| **微信** | 扫描下方二维码添加 |

<p align="center">
  <img src="docs/wechat-qrcode.png" alt="微信二维码" width="240">
</p>

> 可提供：完整系统部署指导、后端 API 对接、功能定制开发、新平台接入（如乐天、Amazon 等）、仓库 APP 开发等服务。

---

## 参与贡献

1. Fork 本仓库
2. 新建功能分支（`feat/xxx`）
3. 提交代码
4. 新建 Pull Request

---

## 许可证

本项目采用 [MIT License](LICENSE) 开源。
