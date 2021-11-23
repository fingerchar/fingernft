DROP TABLE IF EXISTS `fc_admin_log`;

CREATE TABLE `fc_admin_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '管理员',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '管理员地址',
  `type` int DEFAULT NULL COMMENT '操作分类',
  `action` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作动作',
  `status` tinyint(1) DEFAULT NULL COMMENT '操作状态',
  `result` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作结果，或者成功消息，或者失败消息',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '补充信息',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Table structure for table `fc_admin_notice` */

DROP TABLE IF EXISTS `fc_admin_notice`;

CREATE TABLE `fc_admin_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `notice_id` bigint DEFAULT NULL COMMENT '通知ID',
  `notice_title` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '通知标题',
  `admin_id` bigint DEFAULT NULL COMMENT '接收通知的管理员ID',
  `read_time` bigint DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Table structure for table `fc_admin_permission` */

DROP TABLE IF EXISTS `fc_admin_permission`;

CREATE TABLE `fc_admin_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `permission` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb3;

/*Table structure for table `fc_admin_role` */

DROP TABLE IF EXISTS `fc_admin_role`;

CREATE TABLE `fc_admin_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `desc` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色描述',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `is_system` tinyint(1) DEFAULT '0',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

/*Table structure for table `fc_admin_storage` */

DROP TABLE IF EXISTS `fc_admin_storage`;

CREATE TABLE `fc_admin_storage` (
  `id` bigint(20) unsigned zerofill NOT NULL,
  `key` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件的唯一索引',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名',
  `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件类型',
  `size` int NOT NULL COMMENT '文件大小',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件访问链接',
  `ipfsHash` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Table structure for table `fc_admin_token` */

DROP TABLE IF EXISTS `fc_admin_token`;

CREATE TABLE `fc_admin_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT '0',
  `key` varchar(255) DEFAULT NULL,
  `value` text,
  `minutes` int DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '1 在线：0 下线',
  `shop_id` int NOT NULL DEFAULT '0' COMMENT '门店ID',
  `department_id` int NOT NULL DEFAULT '0' COMMENT '部门id',
  `org_id` int NOT NULL DEFAULT '0' COMMENT '队列ID',
  `deleted` tinyint(1) DEFAULT '0',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Table structure for table `fc_admin_user` */

DROP TABLE IF EXISTS `fc_admin_user`;

CREATE TABLE `fc_admin_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '头像图片',
  `username` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理员名称',
  `nickname` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `password` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理员密码',
  `last_login_ip` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '最近一次登录IP地址',
  `last_login_time` bigint DEFAULT NULL,
  `role_ids` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '[]' COMMENT '角色列表',
  `phone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `department_id` bigint NOT NULL DEFAULT '0' COMMENT '部门id',
  `job_id` bigint DEFAULT NULL COMMENT '对应job表中职位',
  `wechat` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '微信号',
  `wechat_qrcode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '微信二维码',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用：0否，1是',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

/*Table structure for table `fc_contract` */

DROP TABLE IF EXISTS `fc_contract`;

CREATE TABLE `fc_contract` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'nft name',
  `symbol` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'nft symbol',
  `address` varchar(50) DEFAULT NULL COMMENT '合约地址',
  `type` int DEFAULT NULL COMMENT '类型 3:ERC721 2.ERC1155',
  `short_url` varchar(100) DEFAULT NULL COMMENT '短地址',
  `version` varchar(10) DEFAULT NULL COMMENT '合约版本',
  `cover` varchar(512) DEFAULT NULL COMMENT '图标',
  `storage_id` bigint DEFAULT NULL COMMENT '图标保存Id',
  `owner` varchar(50) DEFAULT NULL COMMENT '合约拥有者',
  `is_admin` tinyint(1) DEFAULT '0' COMMENT '是否是官方合约',
  `verify` tinyint(1) DEFAULT '0' COMMENT '是否已验证',
  `description` varchar(1000) DEFAULT NULL COMMENT '合约描述',
  `last_token_id` bigint DEFAULT NULL COMMENT '上一次增发的tokenId',
  `banner_url` varchar(512) DEFAULT '',
  `get_info_times` int DEFAULT '0' COMMENT '获取name和symbol次数',
  `is_royalties` tinyint(1) DEFAULT NULL COMMENT '是否支持版权',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index2` (`address`),
  KEY `index3` (`symbol`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_contract_nft` */

DROP TABLE IF EXISTS `fc_contract_nft`;

CREATE TABLE `fc_contract_nft` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `contract_id` bigint DEFAULT NULL COMMENT '合约id',
  `address` varchar(50) DEFAULT NULL COMMENT '合约地址',
  `category_id` bigint DEFAULT NULL COMMENT '分类id',
  `img_url` varchar(512) DEFAULT NULL COMMENT '图片地址',
  `storage_id` bigint DEFAULT NULL COMMENT '图片保存Id',
  `token_id` varchar(256) DEFAULT NULL COMMENT '对应tokenId',
  `quantity` bigint DEFAULT '0' COMMENT '数量',
  `locked` tinyint(1) DEFAULT NULL COMMENT '是否锁定',
  `locked_content` varchar(512) DEFAULT NULL COMMENT '锁定描述',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `royalties` varchar(512) DEFAULT NULL COMMENT '版权',
  `properties` varchar(1024) DEFAULT NULL COMMENT '属性',
  `nft_verify` int DEFAULT '0' COMMENT '是否已验证',
  `is_sync` tinyint(1) DEFAULT '0' COMMENT '是否已同步链',
  `type` int DEFAULT NULL COMMENT '类型',
  `creator` varchar(50) DEFAULT NULL COMMENT 'nft拥有者',
  `tx_hash` varchar(100) DEFAULT NULL COMMENT '区块链交易hash值',
  `anim_url` varchar(512) DEFAULT NULL COMMENT '视频音频地址',
  `anim_storage_id` bigint DEFAULT NULL COMMENT '视频音频地址id',
  `metadata_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '资源地址',
  `metadata_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '资源内容',
  `get_meta_times` int DEFAULT '0' COMMENT '获取资源次数',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`address`),
  KEY `index3` (`creator`),
  KEY `index4` (`token_id`),
  KEY `index5` (`is_sync`),
  KEY `index6` (`create_time`),
  FULLTEXT KEY `index7` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_nft_category` */

DROP TABLE IF EXISTS `fc_nft_category`;

CREATE TABLE `fc_nft_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `name` varchar(100) NOT NULL COMMENT '类目名称',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  `order` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_nft_items` */

DROP TABLE IF EXISTS `fc_nft_items`;

CREATE TABLE `fc_nft_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `nft_id` bigint NOT NULL COMMENT 'nft  id',
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `img_url` varchar(512) DEFAULT NULL COMMENT '图片资源地址',
  `storage_id` bigint DEFAULT NULL COMMENT '图片资源保存id',
  `price` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '价格',
  `usdt_price` decimal(20,4) DEFAULT '0.0000' COMMENT 'usdt价格',
  `paytoken_address` varchar(50) DEFAULT NULL COMMENT '支付方式地址',
  `sell_quantity` bigint DEFAULT NULL COMMENT '售卖数量',
  `quantity` bigint DEFAULT NULL COMMENT '数量',
  `signature` varchar(512) DEFAULT NULL COMMENT '签名',
  `item_owner` varchar(50) NOT NULL COMMENT 'token拥有者',
  `onsell` tinyint(1) DEFAULT NULL COMMENT '是否在售',
  `category_id` bigint DEFAULT '0',
  `address` varchar(50) DEFAULT NULL,
  `is_sync` tinyint(1) DEFAULT '0',
  `token_id` varchar(256) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`item_owner`),
  KEY `index3` (`address`),
  KEY `index4` (`nft_id`),
  KEY `index5` (`is_sync`),
  KEY `index6` (`onsell`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_nft_like` */

DROP TABLE IF EXISTS `fc_nft_like`;

CREATE TABLE `fc_nft_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `nft_id` bigint NOT NULL DEFAULT '0' COMMENT 'nft id',
  `user_id` bigint DEFAULT '0' COMMENT '用户id',
  `user_address` varchar(50) NOT NULL COMMENT '用户地址',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index3` (`nft_id`,`user_address`),
  KEY `index1` (`nft_id`),
  KEY `index4` (`user_address`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_notice` */

DROP TABLE IF EXISTS `fc_notice`;

CREATE TABLE `fc_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `type` char(1) DEFAULT NULL COMMENT '大类型1follow 2like 3trade',
  `sub_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '消息类型：0: "LIKE"\\n1: "FOLLOWING"\\n2: "ORDER"\\n3: "BID"\\n4: "BUY"\\n5: "SALE"\\n6: "CANCEL"\\n7: "CANCEL_BID"\\n8: "TRANSFER"\\n9: "MINT"\\n10: "BURN"',
  `user_id` bigint DEFAULT '0' COMMENT '消息对应用户',
  `owner` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '消息对应用户地址',
  `operator` varchar(50) DEFAULT '' COMMENT '操作者地址',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '消息内容',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  `image` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '显示图片',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '显示名称',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`owner`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_order` */

DROP TABLE IF EXISTS `fc_order`;

CREATE TABLE `fc_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表ID',
  `owner` varchar(50) DEFAULT NULL COMMENT 'order发起者',
  `sell_token` varchar(50) DEFAULT NULL COMMENT '售卖token地址',
  `sell_token_id` varchar(256) DEFAULT NULL COMMENT '售卖token id',
  `sell_type` int DEFAULT NULL COMMENT '售卖token类型',
  `sell_value` varchar(256) DEFAULT NULL COMMENT '售卖token价格',
  `buyer_token` varchar(50) DEFAULT NULL COMMENT '购买token地址',
  `buyer_token_id` varchar(256) DEFAULT NULL COMMENT '购买token id',
  `buyer_type` int DEFAULT NULL COMMENT '购买token类型',
  `buyer_value` varchar(256) DEFAULT NULL COMMENT '购买token价格',
  `salt` varchar(100) DEFAULT NULL COMMENT '盐',
  `signature` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '签名',
  `status` int DEFAULT '0' COMMENT '状态0 未完成， 1完成',
  `expired` tinyint(1) DEFAULT '0' COMMENT '是否过期',
  `order_type` int DEFAULT NULL COMMENT '订单类型',
  `usdt_price` varchar(64) DEFAULT NULL COMMENT 'usdt价格',
  `sells` bigint DEFAULT '0' COMMENT '已经售卖数量',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`owner`),
  KEY `index3` (`sell_token`),
  KEY `index4` (`sell_token_id`),
  KEY `index5` (`buyer_token`),
  KEY `index6` (`buyer_token_id`),
  KEY `index7` (`salt`),
  KEY `index8` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_order_log` */

DROP TABLE IF EXISTS `fc_order_log`;

CREATE TABLE `fc_order_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `order_id` bigint DEFAULT NULL COMMENT '订单id',
  `from` varchar(50) DEFAULT NULL COMMENT '订单发起人地址',
  `to` varchar(50) DEFAULT NULL COMMENT '交易对象地址',
  `type` int DEFAULT NULL COMMENT '交易类型sale_cancel, sale_buy, bid_cancel, bid_accept, erc721_create, erc1155_create, token_tranfer, token_burn',
  `tx_hash` varchar(100) DEFAULT NULL COMMENT '交易hash',
  `pre_log_id` bigint DEFAULT NULL COMMENT '前一个订单日志id',
  `token` varchar(50) DEFAULT NULL COMMENT '操作NFT的地址',
  `token_id` varchar(256) DEFAULT NULL COMMENT '操作NFT的tokenId',
  `content` mediumtext COMMENT '日志体',
  `expired` tinyint(1) DEFAULT '0' COMMENT '是否过期',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`order_id`),
  KEY `index3` (`type`),
  KEY `index4` (`token`),
  KEY `index5` (`token_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_pay_token` */

DROP TABLE IF EXISTS `fc_pay_token`;

CREATE TABLE `fc_pay_token` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `name` varchar(100) NOT NULL COMMENT 'token名称',
  `symbol` varchar(100) NOT NULL COMMENT 'token symbol',
  `decimals` int NOT NULL COMMENT 'token 精度',
  `rate` decimal(24,16) DEFAULT NULL COMMENT 'token 汇率',
  `address` varchar(50) DEFAULT NULL COMMENT 'token 合约地址',
  `img_url` varchar(512) DEFAULT NULL COMMENT 'token 图像',
  `storage_id` bigint DEFAULT NULL COMMENT 'token 图像保存id',
  `type` int DEFAULT NULL COMMENT '0 ETH 1 WETH 2 ERC1155 3 ERC721 4 ERC721Deprecated',
  `is_default` tinyint NOT NULL DEFAULT '0' COMMENT '默认支付类型',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_storage` */

DROP TABLE IF EXISTS `fc_storage`;

CREATE TABLE `fc_storage` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `key` varchar(63) NOT NULL COMMENT '文件的唯一索引',
  `name` varchar(255) NOT NULL COMMENT '文件名',
  `type` varchar(20) NOT NULL COMMENT '文件类型',
  `size` int NOT NULL COMMENT '文件大小',
  `url` varchar(255) DEFAULT NULL COMMENT '文件访问链接',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `ipfsHash` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件存储表';

/*Table structure for table `fc_system` */

DROP TABLE IF EXISTS `fc_system`;

CREATE TABLE `fc_system` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `key_name` varchar(255) NOT NULL COMMENT '系统配置名',
  `key_value` varchar(255) NOT NULL COMMENT '系统配置值',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index2` (`key_name`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';

/*Table structure for table `fc_tx_order` */

DROP TABLE IF EXISTS `fc_tx_order`;

CREATE TABLE `fc_tx_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `order_id` bigint DEFAULT NULL COMMENT '订单id',
  `tx_hash` varchar(100) DEFAULT NULL COMMENT '交易hash',
  `from` varchar(50) DEFAULT NULL COMMENT '卖方',
  `to` varchar(50) DEFAULT NULL COMMENT '买方',
  `token` varchar(50) DEFAULT NULL COMMENT 'token地址',
  `token_id` varchar(256) DEFAULT NULL COMMENT 'tokenid',
  `type` int DEFAULT NULL COMMENT '交易类型：sale_cancel, sale_buy, bid_cancel, bid_accept, erc721_create, erc1155_create, token_tranfer, token_burn',
  `nonce` bigint DEFAULT NULL COMMENT '交易序列号',
  `input` text COMMENT '交易输入信息',
  `salt` varchar(100) DEFAULT NULL COMMENT '盐',
  `is_sync` tinyint(1) DEFAULT '0' COMMENT '是否已经同步',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index2` (`tx_hash`),
  KEY `index3` (`salt`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_user` */

DROP TABLE IF EXISTS `fc_user`;

CREATE TABLE `fc_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `nickname` varchar(100) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `address` varchar(50) NOT NULL DEFAULT '' COMMENT '钱包账号地址',
  `login_type` char(1) NOT NULL DEFAULT '1' COMMENT '登录类型',
  `last_login_time` bigint DEFAULT NULL,
  `last_login_ip` varchar(63) DEFAULT NULL COMMENT '最后登录ip',
  `user_verify` int DEFAULT '0' COMMENT '用户是否验证',
  `short_url` varchar(512) DEFAULT NULL COMMENT '用户短地址',
  `brief` varchar(200) DEFAULT NULL COMMENT '用户简介',
  `banner_url` varchar(512) DEFAULT '',
  `is_web` tinyint(1) DEFAULT NULL COMMENT '是否属于本站用户',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`address`),
  KEY `index3` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_user_details` */

DROP TABLE IF EXISTS `fc_user_details`;

CREATE TABLE `fc_user_details` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `user_address` varchar(50) NOT NULL COMMENT '用户地址',
  `email` varchar(256) DEFAULT NULL COMMENT '邮箱',
  `twitter` varchar(256) DEFAULT NULL COMMENT '推特账号',
  `wechart` varchar(256) DEFAULT NULL COMMENT '微信账号',
  `qq` varchar(50) DEFAULT NULL COMMENT 'QQ账号',
  `type` char(1) DEFAULT NULL COMMENT '验证类型 1.个人，2公司',
  `company` varchar(256) DEFAULT NULL COMMENT '公司名称',
  `address` varchar(512) DEFAULT NULL COMMENT '地址（个人地址或公司地址）',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index2` (`user_id`),
  KEY `index4` (`user_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_user_follow` */

DROP TABLE IF EXISTS `fc_user_follow`;

CREATE TABLE `fc_user_follow` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `user_id` bigint DEFAULT '0' COMMENT '用户id',
  `user_address` varchar(50) NOT NULL COMMENT '用户地址',
  `following_id` bigint DEFAULT '0' COMMENT '关注人id',
  `following_address` varchar(50) NOT NULL COMMENT '关注人地址',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index3` (`user_address`,`following_address`),
  KEY `index1` (`user_address`),
  KEY `index2` (`following_address`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `fc_user_log` */

DROP TABLE IF EXISTS `fc_user_log`;

CREATE TABLE `fc_user_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户地址',
  `type` int DEFAULT '0' COMMENT '登录类型1：登录，2: 退出',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户ip地址',
  `action` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作动作',
  `status` tinyint(1) DEFAULT NULL COMMENT '操作状态',
  `result` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作结果，或者成功消息，或者失败消息',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户登录日志';

/*Table structure for table `fc_user_token` */

DROP TABLE IF EXISTS `fc_user_token`;

CREATE TABLE `fc_user_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_address` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户钱包地址',
  `user_token` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户登录token',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户登录token表';

