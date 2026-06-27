-- ============================================================
-- 代代米日淘 · PC 出入库客户端本地数据库
-- Database: recpiptuserdb
-- 说明: 由 代代米日淘出入库PC 中 ReceiptDAL 的 SQL 语句确认
-- ============================================================

CREATE DATABASE IF NOT EXISTS `recpiptuserdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `recpiptuserdb`;

-- -----------------------------------------------------------
-- 入库扫描记录
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `records` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `UserID` VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
  `Userphone` VARCHAR(32) DEFAULT NULL COMMENT '用户手机号',
  `Ordernumber` VARCHAR(64) DEFAULT NULL COMMENT '订单号',
  `ProductID` VARCHAR(64) DEFAULT NULL COMMENT '商品编号',
  `ProductName` VARCHAR(512) DEFAULT NULL COMMENT '商品名称',
  `price` VARCHAR(32) DEFAULT NULL COMMENT '价格',
  `MerCari` VARCHAR(32) DEFAULT NULL COMMENT '平台标识 Yahoo/Mercari',
  `Remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
  `Time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  `Status` VARCHAR(32) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`Id`),
  KEY `idx_ordernumber` (`Ordernumber`),
  KEY `idx_productid` (`ProductID`),
  KEY `idx_time` (`Time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PC端入库扫描记录';

-- -----------------------------------------------------------
-- 出库记录
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `boundtable` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `NameNumber` VARCHAR(64) DEFAULT NULL COMMENT '包裹编号',
  `NameSum` VARCHAR(32) DEFAULT NULL COMMENT '件数',
  `NameWeight` VARCHAR(32) DEFAULT NULL COMMENT '重量',
  `Companies` VARCHAR(128) DEFAULT NULL COMMENT '物流公司',
  `Ordernumber` VARCHAR(128) DEFAULT NULL COMMENT '物流单号',
  `Remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
  `Time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '出库时间',
  PRIMARY KEY (`Id`),
  KEY `idx_namenumber` (`NameNumber`),
  KEY `idx_time` (`Time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PC端出库记录';

-- -----------------------------------------------------------
-- PC 端本地登录（可选，也可使用后台 admin 接口登录）
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(64) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PC出入库本地账号';

INSERT INTO `users` (`username`, `password`) VALUES ('admin', 'admin123');
