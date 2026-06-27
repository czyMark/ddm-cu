# 数据库说明

本项目使用两个 MySQL 数据库：

| 数据库 | 脚本 | 用途 |
|--------|------|------|
| `yahoo` | [yahoo.sql](./yahoo.sql) | 主业务：用户、订单、支付、CMS 等 |
| `recpiptuserdb` | [recpiptuserdb.sql](./recpiptuserdb.sql) | PC 出入库客户端本地扫描记录 |

## 导入方式

```bash
mysql -u root -p < database/yahoo.sql
mysql -u root -p < database/recpiptuserdb.sql
```

## 注意事项

1. **`yahoo.sql` 为反推脚本**：表名/字段名根据前端 API 与后端 Controller 推断，可能与线上生产库存在细微差异。完整精确结构需 `demo-service-logic` 模块或生产库导出。
2. **默认账号**：两个库均预置 `admin / admin123`，部署后请立即修改。
3. **后端配置**：修改 `后端服务/src/main/resources/application-druid.yml` 中的数据库连接。
4. **PC 客户端配置**：修改 `代代米日淘出入库PC/WinReceipt/ReceiptDAL/SqlHelper.cs` 中的连接字符串。

## 雅虎订单状态 (goodorder.type)

| 值 | 含义 |
|----|------|
| 1 | 出价中 |
| 2 | 竞拍成功（待补差价） |
| 3 | 竞拍失败 |
| 4 | 待入库 |
| 5 | 已入库 |
| 6 | 申请发货 |
| 7 | 待付尾款 |
| 8 | 待发货 |
| 9 | 已发货 |
| 10 | 被超越 |
| 100 | 预约出价 |

## 煤炉订单状态 (merorder.type)

| 值 | 含义 |
|----|------|
| 1 | 待入库 |
| 2 | 已入库 |
| 3 | 申请发货 |
| 4 | 待付尾款 |
| 5 | 待发货 |
| 6 | 已发货 |
| 7 | 已退款 |
