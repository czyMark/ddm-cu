-- ============================================================
-- 代代米日淘 · 主业务数据库
-- Database: yahoo
-- 说明: 根据前端 API、后端 Controller、后台管理字段反推生成
--       表名/字段名可能与线上略有差异，导入后请对照 demo-service-logic 微调
-- ============================================================

CREATE DATABASE IF NOT EXISTS `yahoo` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `yahoo`;

-- -----------------------------------------------------------
-- 用户表
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `userid` INT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` VARCHAR(64) NOT NULL COMMENT '微信openid',
  `account` VARCHAR(64) DEFAULT NULL COMMENT '账号',
  `nickName` VARCHAR(128) DEFAULT NULL COMMENT '微信昵称',
  `avatarUrl` VARCHAR(512) DEFAULT NULL COMMENT '头像',
  `realname` VARCHAR(64) DEFAULT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `twophone` VARCHAR(20) DEFAULT NULL COMMENT '备用手机号',
  `loginphone` VARCHAR(20) DEFAULT NULL COMMENT '登录手机号',
  `balance` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '账户余额(円)',
  `yjBalance` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '竞拍保证金(円)',
  `invitecode` VARCHAR(32) DEFAULT NULL COMMENT '注册时使用的邀请码',
  `myinvitecode` VARCHAR(32) DEFAULT NULL COMMENT '自己的邀请码',
  `myxfprice` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '本人累计消费',
  `xjxfprice` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '下级累计消费',
  `yjtxprice` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '已提现佣金',
  `isAdmin` TINYINT NOT NULL DEFAULT 0 COMMENT '是否小程序管理员 0否1是',
  `isRead` TINYINT NOT NULL DEFAULT 1 COMMENT '客服消息是否已读 0未读1已读',
  `contact` TEXT COMMENT '客服留言',
  `remark` VARCHAR(512) DEFAULT NULL COMMENT '管理员备注',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `uk_openid` (`openid`),
  KEY `idx_loginphone` (`loginphone`),
  KEY `idx_myinvitecode` (`myinvitecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='C端用户';

-- -----------------------------------------------------------
-- 收货地址
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `useraddress` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `username` VARCHAR(64) NOT NULL COMMENT '收件人',
  `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `address` VARCHAR(512) NOT NULL COMMENT '地址 region----detail',
  `idZ` VARCHAR(512) DEFAULT NULL COMMENT '身份证正面',
  `idF` VARCHAR(512) DEFAULT NULL COMMENT '身份证反面',
  `idNo` VARCHAR(32) DEFAULT NULL COMMENT '身份证号',
  `defaultType` TINYINT NOT NULL DEFAULT 0 COMMENT '1默认地址',
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收货地址';

-- -----------------------------------------------------------
-- 雅虎拍卖订单
-- type: 1出价中 2竞拍成功 3失败 4待入库 5已入库 6申请发货 7待付尾款 8待发货 9已发货 10被超越 100预约出价
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `goodorder` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orderid` VARCHAR(64) NOT NULL COMMENT '订单号',
  `userid` INT NOT NULL,
  `parentid` INT DEFAULT NULL COMMENT '合并订单父ID',
  `goodCode` VARCHAR(64) NOT NULL COMMENT '雅虎商品编号',
  `title` VARCHAR(512) DEFAULT NULL,
  `imageurl` VARCHAR(512) DEFAULT NULL,
  `price` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '出价/成交价(円)',
  `nowprice` DECIMAL(12,2) DEFAULT NULL COMMENT '当前竞拍价',
  `firstPayPrice` DECIMAL(12,2) DEFAULT NULL COMMENT '首笔支付金额',
  `type` TINYINT NOT NULL DEFAULT 1 COMMENT '订单状态',
  `ratetype` TINYINT NOT NULL DEFAULT 0 COMMENT '0不含税 1含税',
  `count` INT NOT NULL DEFAULT 1,
  `transactionid` VARCHAR(128) DEFAULT NULL COMMENT '微信支付流水号',
  `addressid` INT DEFAULT NULL,
  `accountid` INT DEFAULT NULL COMMENT '雅虎代购账号 1babyface',
  `jpyunfeiType` TINYINT NOT NULL DEFAULT 0 COMMENT '0非到付 1到付',
  `dgPrice` DECIMAL(12,2) DEFAULT 0 COMMENT '代购费',
  `jpyunfei` DECIMAL(12,2) DEFAULT 0 COMMENT '岛内运费',
  `gjyunfei` DECIMAL(12,2) DEFAULT 0 COMMENT '国际运费',
  `cangcufei` DECIMAL(12,2) DEFAULT 0 COMMENT '仓储费',
  `baozhuangfei` DECIMAL(12,2) DEFAULT 0 COMMENT '包装费',
  `shouxufei` DECIMAL(12,2) DEFAULT 0 COMMENT '手续费',
  `otherPrice` DECIMAL(12,2) DEFAULT 0 COMMENT '其他费用',
  `otherPriceContent` VARCHAR(512) DEFAULT NULL COMMENT '其他费用说明',
  `luxian` VARCHAR(256) DEFAULT NULL COMMENT '邮寄路线',
  `lxRemarks` VARCHAR(512) DEFAULT NULL COMMENT '路线备注',
  `wlcompany` VARCHAR(128) DEFAULT NULL COMMENT '物流公司',
  `trackingNumber` VARCHAR(128) DEFAULT NULL COMMENT '物流单号',
  `wlRemarks` VARCHAR(512) DEFAULT NULL COMMENT '物流备注',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `paytime` DATETIME DEFAULT NULL,
  `updateTime` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_orderid` (`orderid`),
  KEY `idx_userid` (`userid`),
  KEY `idx_type` (`type`),
  KEY `idx_parentid` (`parentid`),
  KEY `idx_goodCode` (`goodCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='雅虎拍卖订单';

-- -----------------------------------------------------------
-- 煤炉直购订单
-- type: 1待入库 2已入库 3申请发货 4待付尾款 5待发货 6已发货 7已退款
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `merorder` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orderid` VARCHAR(64) NOT NULL,
  `userid` INT NOT NULL,
  `parentid` INT DEFAULT NULL,
  `nickName` VARCHAR(128) DEFAULT NULL,
  `goodCode` VARCHAR(64) NOT NULL,
  `title` VARCHAR(512) DEFAULT NULL,
  `imageurl` VARCHAR(512) DEFAULT NULL,
  `price` DECIMAL(12,2) NOT NULL DEFAULT 0,
  `firstPayPrice` DECIMAL(12,2) DEFAULT NULL,
  `count` INT NOT NULL DEFAULT 1,
  `type` TINYINT NOT NULL DEFAULT 1,
  `transactionid` VARCHAR(128) DEFAULT NULL,
  `addressid` INT DEFAULT NULL,
  `dgPrice` DECIMAL(12,2) DEFAULT 0,
  `jpyunfei` DECIMAL(12,2) DEFAULT 0,
  `gjyunfei` DECIMAL(12,2) DEFAULT 0,
  `cangcufei` DECIMAL(12,2) DEFAULT 0,
  `baozhuangfei` DECIMAL(12,2) DEFAULT 0,
  `shouxufei` DECIMAL(12,2) DEFAULT 0,
  `otherPrice` DECIMAL(12,2) DEFAULT 0,
  `otherPriceContent` VARCHAR(512) DEFAULT NULL,
  `luxian` VARCHAR(256) DEFAULT NULL,
  `lxRemarks` VARCHAR(512) DEFAULT NULL,
  `wlcompany` VARCHAR(128) DEFAULT NULL,
  `trackingNumber` VARCHAR(128) DEFAULT NULL,
  `wlRemarks` VARCHAR(512) DEFAULT NULL,
  `time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `updateTime` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_orderid` (`orderid`),
  KEY `idx_userid` (`userid`),
  KEY `idx_type` (`type`),
  KEY `idx_parentid` (`parentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='煤炉直购订单';

-- -----------------------------------------------------------
-- 煤炉购物车
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `mrshppingcar` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `goodcode` VARCHAR(64) NOT NULL,
  `imageurl` VARCHAR(512) DEFAULT NULL,
  `title` VARCHAR(512) DEFAULT NULL,
  `price` DECIMAL(12,2) DEFAULT 0,
  `count` INT NOT NULL DEFAULT 1,
  `firstPayPrice` DECIMAL(12,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userid`),
  KEY `idx_goodcode` (`goodcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='煤炉购物车';

-- -----------------------------------------------------------
-- 余额变更记录
-- type: 1充值 2消费 3提现 4解冻
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `balanceupdata` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `balance` DECIMAL(12,2) NOT NULL,
  `type` TINYINT NOT NULL,
  `des` VARCHAR(128) DEFAULT NULL COMMENT '描述',
  `date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userid`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='余额变更记录';

-- -----------------------------------------------------------
-- 竞拍保证金
-- type: 0已充值 1申请解冻 2已退款 3驳回
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `deposit` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `balance` DECIMAL(12,2) NOT NULL,
  `transactionid` VARCHAR(128) DEFAULT NULL,
  `type` TINYINT NOT NULL DEFAULT 0,
  `date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userid`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='竞拍保证金记录';

-- -----------------------------------------------------------
-- 提现记录
-- type: 1提现中 2成功 3失败
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `txrecord` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `balance` DECIMAL(12,2) NOT NULL,
  `type` TINYINT NOT NULL DEFAULT 1,
  `des` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userid`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录';

-- -----------------------------------------------------------
-- 收藏
-- platform: 0雅虎 1煤炉
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `collect` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `platform` TINYINT NOT NULL DEFAULT 0,
  `goodcode` VARCHAR(64) NOT NULL,
  `imageUrl` VARCHAR(512) DEFAULT NULL,
  `title` VARCHAR(512) DEFAULT NULL,
  `counts` VARCHAR(32) DEFAULT NULL COMMENT '出价次数展示',
  `price` DECIMAL(12,2) DEFAULT 0,
  `type` TINYINT NOT NULL DEFAULT 1 COMMENT '0失效 1正常',
  PRIMARY KEY (`id`),
  KEY `idx_userid_platform` (`userid`, `platform`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品收藏';

-- -----------------------------------------------------------
-- 浏览足迹
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `liulan` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `platform` TINYINT NOT NULL DEFAULT 0,
  `goodcode` VARCHAR(64) NOT NULL,
  `imageUrl` VARCHAR(512) DEFAULT NULL,
  `title` VARCHAR(512) DEFAULT NULL,
  `counts` VARCHAR(32) DEFAULT NULL,
  `price` DECIMAL(12,2) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='浏览足迹';

-- -----------------------------------------------------------
-- 后台员工账号
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `admin` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(64) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `type` INT NOT NULL DEFAULT 0 COMMENT '角色等级',
  `authtype` TEXT COMMENT '权限菜单JSON',
  `token` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台员工';

-- 默认管理员 admin / admin123（部署后请立即修改）
INSERT INTO `admin` (`username`, `password`, `type`, `authtype`) VALUES
('admin', 'admin123', 1, '[]');

-- -----------------------------------------------------------
-- 雅虎/煤炉 Cookie（爬虫代购账号）
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cookie` TEXT COMMENT '平台登录Cookie',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台Cookie';

INSERT INTO `account` (`cookie`) VALUES ('');

-- -----------------------------------------------------------
-- CMS 内容
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `image` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(512) NOT NULL COMMENT '轮播图地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页轮播图';

CREATE TABLE IF NOT EXISTS `notice` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(256) NOT NULL,
  `content` MEDIUMTEXT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告';

CREATE TABLE IF NOT EXISTS `question` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(256) NOT NULL,
  `content` MEDIUMTEXT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='常见问题';

CREATE TABLE IF NOT EXISTS `settings` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `customer` MEDIUMTEXT COMMENT '客服联系方式HTML',
  `usergreement` MEDIUMTEXT COMMENT '用户协议',
  `biddingrules` MEDIUMTEXT COMMENT '竞拍规则',
  `privacy` MEDIUMTEXT COMMENT '隐私政策',
  `chujiarules` MEDIUMTEXT COMMENT '出价规则',
  `riskwarning` MEDIUMTEXT COMMENT '风险提示'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帮助与规则设置';

INSERT INTO `settings` (`customer`, `usergreement`, `biddingrules`, `privacy`, `chujiarules`, `riskwarning`)
VALUES ('', '', '', '', '', '');

CREATE TABLE IF NOT EXISTS `jprate` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rate` DECIMAL(10,4) NOT NULL DEFAULT 0.0500 COMMENT 'CNY/JPY 汇率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='中日汇率';

INSERT INTO `jprate` (`rate`) VALUES (0.0500);

CREATE TABLE IF NOT EXISTS `fenlei` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` LONGTEXT COMMENT '雅虎/煤炉分类JSON',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类';

INSERT INTO `fenlei` (`content`) VALUES ('{}');

-- -----------------------------------------------------------
-- 客服消息（可选）
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `contact` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `content` TEXT,
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户客服消息';
