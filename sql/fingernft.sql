/*
SQLyog Ultimate
MySQL - 8.0.15 : Database - fingernft
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `fc_admin_log` */

DROP TABLE IF EXISTS `fc_admin_log`;

CREATE TABLE `fc_admin_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '管理员',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '管理员地址',
  `type` int(11) DEFAULT NULL COMMENT '操作分类',
  `action` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作动作',
  `status` tinyint(1) DEFAULT NULL COMMENT '操作状态',
  `result` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作结果，或者成功消息，或者失败消息',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '补充信息',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8;

/*Data for the table `fc_admin_log` */

/*Table structure for table `fc_admin_notice` */

DROP TABLE IF EXISTS `fc_admin_notice`;

CREATE TABLE `fc_admin_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notice_id` bigint(20) DEFAULT NULL COMMENT '通知ID',
  `notice_title` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '通知标题',
  `admin_id` bigint(20) DEFAULT NULL COMMENT '接收通知的管理员ID',
  `read_time` bigint(20) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `fc_admin_notice` */

/*Table structure for table `fc_admin_permission` */

DROP TABLE IF EXISTS `fc_admin_permission`;

CREATE TABLE `fc_admin_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `permission` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8;

/*Data for the table `fc_admin_permission` */

insert  into `fc_admin_permission`(`id`,`role_id`,`permission`,`deleted`,`create_time`,`update_time`) values (1,1,'*',0,NULL,NULL);

/*Table structure for table `fc_admin_role` */

DROP TABLE IF EXISTS `fc_admin_role`;

CREATE TABLE `fc_admin_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `desc` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色描述',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `is_system` tinyint(1) DEFAULT '0',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `fc_admin_role` */

insert  into `fc_admin_role`(`id`,`name`,`desc`,`enabled`,`is_system`,`deleted`,`create_time`,`update_time`) values (1,'超级管理员','所有模块的权限',1,1,0,1546300800,1546300800);

/*Table structure for table `fc_admin_token` */

DROP TABLE IF EXISTS `fc_admin_token`;

CREATE TABLE `fc_admin_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT '0',
  `key` varchar(255) DEFAULT NULL,
  `value` text,
  `minutes` int(11) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1 在线：0 下线',
  `shop_id` int(11) NOT NULL DEFAULT '0' COMMENT '门店ID',
  `department_id` int(11) NOT NULL DEFAULT '0' COMMENT '部门id',
  `org_id` int(11) NOT NULL DEFAULT '0' COMMENT '队列ID',
  `deleted` tinyint(1) DEFAULT '0',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `fc_admin_token` */

insert  into `fc_admin_token`(`id`,`user_id`,`key`,`value`,`minutes`,`status`,`shop_id`,`department_id`,`org_id`,`deleted`,`create_time`,`update_time`) values (4,1,'cdbb1d19-53cb-4dea-88cb-207650b9c537',NULL,NULL,1,0,0,0,0,1639361807,1639378456);

/*Table structure for table `fc_admin_user` */

DROP TABLE IF EXISTS `fc_admin_user`;

CREATE TABLE `fc_admin_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '头像图片',
  `username` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理员名称',
  `nickname` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `password` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理员密码',
  `last_login_ip` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '最近一次登录IP地址',
  `last_login_time` bigint(20) DEFAULT NULL,
  `role_ids` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '[]' COMMENT '角色列表',
  `phone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `department_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '部门id',
  `job_id` bigint(20) DEFAULT NULL COMMENT '对应job表中职位',
  `wechat` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '微信号',
  `wechat_qrcode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '微信二维码',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用：0否，1是',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `fc_admin_user` */

insert  into `fc_admin_user`(`id`,`avatar`,`username`,`nickname`,`password`,`last_login_ip`,`last_login_time`,`role_ids`,`phone`,`department_id`,`job_id`,`wechat`,`wechat_qrcode`,`status`,`deleted`,`create_time`,`update_time`) values (1,'','admin','admin','$2a$10$UH/WT5WEt3ET8TToVGlenOaxFvqRcNU8FifFVEe0kTkbi0kAe5oLe','127.0.0.1',1639378456,'[1]','',0,NULL,'','',0,0,NULL,1639378456);

/*Table structure for table `fc_contract` */

DROP TABLE IF EXISTS `fc_contract`;

CREATE TABLE `fc_contract` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'nft name',
  `symbol` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'nft symbol',
  `address` varchar(50) DEFAULT NULL COMMENT '合约地址',
  `type` int(11) DEFAULT NULL COMMENT '类型 3:ERC721 2.ERC1155',
  `short_url` varchar(100) DEFAULT NULL COMMENT '短地址',
  `version` varchar(10) DEFAULT NULL COMMENT '合约版本',
  `cover` varchar(512) DEFAULT NULL COMMENT '图标',
  `storage_id` bigint(20) DEFAULT NULL COMMENT '图标保存Id',
  `owner` varchar(50) DEFAULT NULL COMMENT '合约拥有者',
  `is_admin` tinyint(1) DEFAULT '0' COMMENT '是否是官方合约',
  `verify` tinyint(1) DEFAULT '0' COMMENT '是否已验证',
  `description` varchar(1000) DEFAULT NULL COMMENT '合约描述',
  `last_token_id` bigint(20) DEFAULT '0' COMMENT '上一次增发的tokenId',
  `banner_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '封面',
  `get_info_times` int(11) DEFAULT '0' COMMENT '获取name和symbol次数',
  `is_royalties` tinyint(1) DEFAULT NULL COMMENT '是否支持版权',
  `signer` varchar(50) DEFAULT NULL COMMENT '签名人账号',
  `is_sync` tinyint(1) DEFAULT '0' COMMENT '是否已经同步',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index2` (`address`),
  KEY `index3` (`symbol`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_contract` */

insert  into `fc_contract`(`id`,`name`,`symbol`,`address`,`type`,`short_url`,`version`,`cover`,`storage_id`,`owner`,`is_admin`,`verify`,`description`,`last_token_id`,`banner_url`,`get_info_times`,`is_royalties`,`signer`,`is_sync`,`deleted`,`create_time`,`update_time`) values (1,'Finger NFT','Finger NFT','0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',3,NULL,NULL,'/static/upload/QmTHpk1sUVAPHQ8KrGeWgHzUdLkHKH1D5JhUxhv1bzsnML.png',1,'0x3fad478f5957f7c09741c090b245c044e4a6ecc0',1,1,'Finger NFT',6,'',0,0,NULL,1,0,1619552382,1639367012);

/*Table structure for table `fc_contract_nft` */

DROP TABLE IF EXISTS `fc_contract_nft`;

CREATE TABLE `fc_contract_nft` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `contract_id` bigint(20) DEFAULT NULL COMMENT '合约id',
  `address` varchar(50) DEFAULT NULL COMMENT '合约地址',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类id',
  `img_url` varchar(512) DEFAULT NULL COMMENT '图片地址',
  `storage_id` bigint(20) DEFAULT NULL COMMENT '图片保存Id',
  `token_id` varchar(256) DEFAULT NULL COMMENT '对应tokenId',
  `quantity` bigint(20) DEFAULT '0' COMMENT '数量',
  `locked` tinyint(1) DEFAULT NULL COMMENT '是否锁定',
  `locked_content` varchar(512) DEFAULT NULL COMMENT '锁定描述',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `royalties` varchar(512) DEFAULT NULL COMMENT '版权',
  `properties` varchar(1024) DEFAULT NULL COMMENT '属性',
  `nft_verify` int(11) DEFAULT '0' COMMENT '是否已验证',
  `is_sync` tinyint(1) DEFAULT '0' COMMENT '是否已同步链',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `creator` varchar(50) DEFAULT NULL COMMENT 'nft拥有者',
  `tx_hash` varchar(100) DEFAULT NULL COMMENT '区块链交易hash值',
  `anim_url` varchar(512) DEFAULT NULL COMMENT '视频音频地址',
  `anim_storage_id` bigint(20) DEFAULT NULL COMMENT '视频音频地址id',
  `metadata_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '资源地址',
  `metadata_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '资源内容',
  `get_meta_times` int(11) DEFAULT '0' COMMENT '获取资源次数',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`address`),
  KEY `index3` (`creator`),
  KEY `index4` (`token_id`),
  KEY `index5` (`is_sync`),
  KEY `index6` (`create_time`),
  FULLTEXT KEY `index7` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_contract_nft` */

insert  into `fc_contract_nft`(`id`,`contract_id`,`address`,`category_id`,`img_url`,`storage_id`,`token_id`,`quantity`,`locked`,`locked_content`,`name`,`description`,`royalties`,`properties`,`nft_verify`,`is_sync`,`type`,`creator`,`tx_hash`,`anim_url`,`anim_storage_id`,`metadata_url`,`metadata_content`,`get_meta_times`,`deleted`,`create_time`,`update_time`) values (107,1,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',15,'/static/uploadQmPs8CApj3TQa3NNd2wnpZMYeuFPBpCyxRp2LGHn1hkQeU.jpeg',250,'1',1,NULL,NULL,'fwe','','[200]','',1,1,3,'0x80dd336deb7eea1923780a7383c806870c9ce27d',NULL,NULL,NULL,'ipfs://ipfs/QmeqNkbcMVBoH7xYpPwMgLrUL9TyLejUWNM5gexxJEbNdA','{\"image\":\"ipfs://ipfs/QmPs8CApj3TQa3NNd2wnpZMYeuFPBpCyxRp2LGHn1hkQeU\",\"animation_url\":\"\",\"external_url\":\"http://192.168.1.8:20004/detail/0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0:1\",\"name\":\"fwe\",\"description\":\"\",\"attributes\":[]}',0,0,1639378246,1639378303);
insert  into `fc_contract_nft`(`id`,`contract_id`,`address`,`category_id`,`img_url`,`storage_id`,`token_id`,`quantity`,`locked`,`locked_content`,`name`,`description`,`royalties`,`properties`,`nft_verify`,`is_sync`,`type`,`creator`,`tx_hash`,`anim_url`,`anim_storage_id`,`metadata_url`,`metadata_content`,`get_meta_times`,`deleted`,`create_time`,`update_time`) values (108,1,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',15,'/static/uploadQmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk/image.gif',253,'2',1,NULL,NULL,'fffe','','[200]','',1,1,3,'0x80dd336deb7eea1923780a7383c806870c9ce27d',NULL,'/static/uploadQmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk/animation.mp4',252,'ipfs://ipfs/QmamKwTwDmMPp6bZgh3CJADP5XPzux1XMWBNcDKfQeZR4v','{\"image\":\"ipfs://ipfs/QmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk/image.gif\",\"animation_url\":\"ipfs://ipfs/QmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk/animation.mp4\",\"external_url\":\"http://192.168.1.8:20004/detail/0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0:2\",\"name\":\"fffe\",\"description\":\"\",\"attributes\":[]}',0,0,1639378505,1639378573);
insert  into `fc_contract_nft`(`id`,`contract_id`,`address`,`category_id`,`img_url`,`storage_id`,`token_id`,`quantity`,`locked`,`locked_content`,`name`,`description`,`royalties`,`properties`,`nft_verify`,`is_sync`,`type`,`creator`,`tx_hash`,`anim_url`,`anim_storage_id`,`metadata_url`,`metadata_content`,`get_meta_times`,`deleted`,`create_time`,`update_time`) values (109,1,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',15,'/static/uploadQmYJEdDX1TV2dW7apj7sU3Rw4dPfNCdf9c6sw3Bhej9S1d.jpeg',255,'3',1,NULL,NULL,'fwe','fwefwefwe','[200]','',1,1,3,'0x80dd336deb7eea1923780a7383c806870c9ce27d',NULL,NULL,NULL,'ipfs://ipfs/QmUE3mbaGk7QobsvN6z5hM2DBz7JJsoWytWeW7FHCRgAeQ','{\"image\":\"ipfs://ipfs/QmYJEdDX1TV2dW7apj7sU3Rw4dPfNCdf9c6sw3Bhej9S1d\",\"animation_url\":\"\",\"external_url\":\"http://192.168.1.8:20004/detail/0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0:3\",\"name\":\"fwe\",\"description\":\"fwefwefwe\",\"attributes\":[]}',0,0,1639378565,1639378633);
insert  into `fc_contract_nft`(`id`,`contract_id`,`address`,`category_id`,`img_url`,`storage_id`,`token_id`,`quantity`,`locked`,`locked_content`,`name`,`description`,`royalties`,`properties`,`nft_verify`,`is_sync`,`type`,`creator`,`tx_hash`,`anim_url`,`anim_storage_id`,`metadata_url`,`metadata_content`,`get_meta_times`,`deleted`,`create_time`,`update_time`) values (110,1,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',15,'/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg',257,'4',1,NULL,NULL,'12','','[200]','',1,1,3,'0x80dd336deb7eea1923780a7383c806870c9ce27d',NULL,NULL,NULL,'ipfs://ipfs/QmZTQu4QyekV3282naCbaM1H8gfGV36Tn9uRHhMfCPeqLH','{\"image\":\"ipfs://ipfs/QmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj\",\"animation_url\":\"\",\"external_url\":\"http://192.168.1.8:20004/detail/0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0:4\",\"name\":\"12\",\"description\":\"\",\"attributes\":[]}',0,0,1639379692,1639379743);
insert  into `fc_contract_nft`(`id`,`contract_id`,`address`,`category_id`,`img_url`,`storage_id`,`token_id`,`quantity`,`locked`,`locked_content`,`name`,`description`,`royalties`,`properties`,`nft_verify`,`is_sync`,`type`,`creator`,`tx_hash`,`anim_url`,`anim_storage_id`,`metadata_url`,`metadata_content`,`get_meta_times`,`deleted`,`create_time`,`update_time`) values (111,1,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',15,'/static/uploadQmfPZzzQVsoTVrG8ixZQkZFLj84mFPi76sB7XwgoDcMxDV.png',259,'5',1,NULL,NULL,'bee','bee  desc','[200]','[{\"key\":\"a\",\"trait_type\":\"a\",\"value\":\"b\"}]',1,1,3,'0xc2c7c3ef46a07806c50a34a0c661cae55afde899',NULL,NULL,NULL,'ipfs://ipfs/QmQPSP9CFFdzPgUyiL6LrESBt6LXAFkG37S6dJA5V9mUJu','{\"image\":\"ipfs://ipfs/QmfPZzzQVsoTVrG8ixZQkZFLj84mFPi76sB7XwgoDcMxDV\",\"animation_url\":\"\",\"external_url\":\"http://192.168.1.8:20004/detail/0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0:5\",\"name\":\"bee\",\"description\":\"bee  desc\",\"attributes\":[{\"value\":\"b\",\"key\":\"a\",\"trait_type\":\"a\"}]}',0,0,1639379917,1639379983);
insert  into `fc_contract_nft`(`id`,`contract_id`,`address`,`category_id`,`img_url`,`storage_id`,`token_id`,`quantity`,`locked`,`locked_content`,`name`,`description`,`royalties`,`properties`,`nft_verify`,`is_sync`,`type`,`creator`,`tx_hash`,`anim_url`,`anim_storage_id`,`metadata_url`,`metadata_content`,`get_meta_times`,`deleted`,`create_time`,`update_time`) values (112,1,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',15,'/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg',261,'6',1,NULL,NULL,'few','','[200]','[{\"key\":\"few\",\"trait_type\":\"few\",\"value\":\"few\"}]',1,1,3,'0x80dd336deb7eea1923780a7383c806870c9ce27d',NULL,NULL,NULL,'ipfs://ipfs/QmQhLXvBH5RYG5J6xL6Uq1YDefhncor7fXtPzhnNpsVTag','{\"image\":\"ipfs://ipfs/QmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj\",\"animation_url\":\"\",\"external_url\":\"http://192.168.1.8:20004/detail/0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0:6\",\"name\":\"few\",\"description\":\"\",\"attributes\":[{\"value\":\"few\",\"key\":\"few\",\"trait_type\":\"few\"}]}',0,0,1639380226,1639380283);

/*Table structure for table `fc_nft_category` */

DROP TABLE IF EXISTS `fc_nft_category`;

CREATE TABLE `fc_nft_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `name` varchar(100) NOT NULL COMMENT '类目名称',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `order` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_nft_category` */

insert  into `fc_nft_category`(`id`,`name`,`deleted`,`create_time`,`update_time`,`order`) values (15,'game',0,1639375831,1639375831,NULL);
insert  into `fc_nft_category`(`id`,`name`,`deleted`,`create_time`,`update_time`,`order`) values (16,'art',0,1639375834,1639375834,NULL);

/*Table structure for table `fc_nft_items` */

DROP TABLE IF EXISTS `fc_nft_items`;

CREATE TABLE `fc_nft_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `nft_id` bigint(20) NOT NULL COMMENT 'nft  id',
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `img_url` varchar(512) DEFAULT NULL COMMENT '图片资源地址',
  `storage_id` bigint(20) DEFAULT NULL COMMENT '图片资源保存id',
  `price` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '价格',
  `usdt_price` decimal(20,4) DEFAULT '0.0000' COMMENT 'usdt价格',
  `paytoken_address` varchar(50) DEFAULT NULL COMMENT '支付方式地址',
  `sell_quantity` bigint(20) DEFAULT NULL COMMENT '售卖数量',
  `quantity` bigint(20) DEFAULT NULL COMMENT '数量',
  `signature` varchar(512) DEFAULT NULL COMMENT '签名',
  `item_owner` varchar(50) NOT NULL COMMENT 'token拥有者',
  `onsell` tinyint(1) DEFAULT NULL COMMENT '是否在售',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT '0',
  `address` varchar(50) DEFAULT NULL,
  `is_sync` tinyint(1) DEFAULT '0',
  `token_id` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`item_owner`),
  KEY `index3` (`address`),
  KEY `index4` (`nft_id`),
  KEY `index5` (`is_sync`),
  KEY `index6` (`onsell`)
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_nft_items` */

insert  into `fc_nft_items`(`id`,`nft_id`,`name`,`description`,`img_url`,`storage_id`,`price`,`usdt_price`,`paytoken_address`,`sell_quantity`,`quantity`,`signature`,`item_owner`,`onsell`,`deleted`,`create_time`,`update_time`,`category_id`,`address`,`is_sync`,`token_id`) values (151,107,'fwe','','/static/uploadQmPs8CApj3TQa3NNd2wnpZMYeuFPBpCyxRp2LGHn1hkQeU.jpeg',250,'1',100.0000,'0xc778417e063141139fce010982780140aa0cd5ab',1,1,'0xe4618fbc1b5116aaf6d18c99496badec7c0427d746fabb550cf2b827528924f870ef226dde729f3e476489fb91abbec059de9488fd6a9964e9e3f1100ab6d8131b','0x80dd336deb7eea1923780a7383c806870c9ce27d',1,0,1639378246,1639378303,15,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',1,'1');
insert  into `fc_nft_items`(`id`,`nft_id`,`name`,`description`,`img_url`,`storage_id`,`price`,`usdt_price`,`paytoken_address`,`sell_quantity`,`quantity`,`signature`,`item_owner`,`onsell`,`deleted`,`create_time`,`update_time`,`category_id`,`address`,`is_sync`,`token_id`) values (153,109,'fwe','fwefwefwe','/static/uploadQmYJEdDX1TV2dW7apj7sU3Rw4dPfNCdf9c6sw3Bhej9S1d.jpeg',255,'0',0.0000,NULL,NULL,1,NULL,'0x80dd336deb7eea1923780a7383c806870c9ce27d',0,0,1639378565,1639378633,15,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',1,'3');
insert  into `fc_nft_items`(`id`,`nft_id`,`name`,`description`,`img_url`,`storage_id`,`price`,`usdt_price`,`paytoken_address`,`sell_quantity`,`quantity`,`signature`,`item_owner`,`onsell`,`deleted`,`create_time`,`update_time`,`category_id`,`address`,`is_sync`,`token_id`) values (154,110,'12','','/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg',257,'0.1',10.0000,'0xc778417e063141139fce010982780140aa0cd5ab',1,1,'0x618384e1b8f352808a8508702e686dbbbd36c339a673fa6a8fc6631ef010b2ff27ea0d1ed4f777ba160d0a3c955d3554b2c9208430fd2dd135afd31b0ff1d3091b','0x80dd336deb7eea1923780a7383c806870c9ce27d',1,0,1639379692,1639379743,15,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',1,'4');
insert  into `fc_nft_items`(`id`,`nft_id`,`name`,`description`,`img_url`,`storage_id`,`price`,`usdt_price`,`paytoken_address`,`sell_quantity`,`quantity`,`signature`,`item_owner`,`onsell`,`deleted`,`create_time`,`update_time`,`category_id`,`address`,`is_sync`,`token_id`) values (155,111,'bee','bee  desc','/static/uploadQmfPZzzQVsoTVrG8ixZQkZFLj84mFPi76sB7XwgoDcMxDV.png',259,'0.3',30.0000,'0x0000000000000000000000000000000000000000',1,1,'0x29346e8d3c5445176a7e826a1fb8f8125a372ba988495b26e3e35a72d367446773db2b9c0d4386534f0c8c65ab82c1c5af2e9f19f531b08046cdf2104cb56c9a1c','0xc2c7c3ef46a07806c50a34a0c661cae55afde899',1,0,1639379917,1639379983,15,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',1,'5');
insert  into `fc_nft_items`(`id`,`nft_id`,`name`,`description`,`img_url`,`storage_id`,`price`,`usdt_price`,`paytoken_address`,`sell_quantity`,`quantity`,`signature`,`item_owner`,`onsell`,`deleted`,`create_time`,`update_time`,`category_id`,`address`,`is_sync`,`token_id`) values (157,112,'few','','/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg',261,'',0.0000,'',0,1,'','0x65957894d458ab763fdf6242986c3388b4b7404b',0,0,1639380553,1639380553,15,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',1,'6');
insert  into `fc_nft_items`(`id`,`nft_id`,`name`,`description`,`img_url`,`storage_id`,`price`,`usdt_price`,`paytoken_address`,`sell_quantity`,`quantity`,`signature`,`item_owner`,`onsell`,`deleted`,`create_time`,`update_time`,`category_id`,`address`,`is_sync`,`token_id`) values (158,108,'fffe','','/static/uploadQmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk/image.gif',253,'',0.0000,'',0,1,'','0x65957894d458ab763fdf6242986c3388b4b7404b',0,0,1639380649,1639380649,15,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',1,'2');

/*Table structure for table `fc_nft_like` */

DROP TABLE IF EXISTS `fc_nft_like`;

CREATE TABLE `fc_nft_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `nft_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'nft id',
  `user_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户地址',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index3` (`nft_id`,`user_address`),
  KEY `index1` (`nft_id`),
  KEY `index4` (`user_address`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_nft_like` */

/*Table structure for table `fc_notice` */

DROP TABLE IF EXISTS `fc_notice`;

CREATE TABLE `fc_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `type` char(1) DEFAULT NULL COMMENT '大类型1follow 2like 3trade',
  `sub_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '消息类型：0: "LIKE"\\n1: "FOLLOWING"\\n2: "ORDER"\\n3: "BID"\\n4: "BUY"\\n5: "SALE"\\n6: "CANCEL"\\n7: "CANCEL_BID"\\n8: "TRANSFER"\\n9: "MINT"\\n10: "BURN"',
  `user_id` bigint(20) DEFAULT '0' COMMENT '消息对应用户',
  `owner` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '消息对应用户地址',
  `operator` varchar(50) DEFAULT '' COMMENT '操作者地址',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '消息内容',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  `image` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '显示图片',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '显示名称',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`owner`)
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_notice` */

insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (265,'3','9',58,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x80dd336deb7eea1923780a7383c806870c9ce27d','{\"tokenId\":1,\"from\":\"0x0000000000000000000000000000000000000000\",\"to\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"txHash\":\"0x41febf6832f77a9d22aca14120a903f7f83327d7991bdef04d7279d9414d7461\",\"token\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\"}',0,'/static/uploadQmPs8CApj3TQa3NNd2wnpZMYeuFPBpCyxRp2LGHn1hkQeU.jpeg','fwe',0,1639378303,1639378303);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (266,'3','9',58,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x80dd336deb7eea1923780a7383c806870c9ce27d','{\"tokenId\":2,\"from\":\"0x0000000000000000000000000000000000000000\",\"to\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"txHash\":\"0x16452a98044f770340040b01f7adfb85e891e7946f8a7813913fb2026d9bdecb\",\"token\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\"}',0,'/static/uploadQmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk/image.gif','fffe',0,1639378573,1639378573);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (267,'3','9',58,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x80dd336deb7eea1923780a7383c806870c9ce27d','{\"tokenId\":3,\"from\":\"0x0000000000000000000000000000000000000000\",\"to\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"txHash\":\"0x237065a3cc5f05d6ca8fbfdc915a7f913af0b2469045eb19c6aa55141fac88e8\",\"token\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\"}',0,'/static/uploadQmYJEdDX1TV2dW7apj7sU3Rw4dPfNCdf9c6sw3Bhej9S1d.jpeg','fwe',0,1639378633,1639378633);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (268,'3','5',58,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x65957894d458ab763fdf6242986c3388b4b7404b','{\"tokenId\":\"1\",\"biderAddr\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"uint\":\"WETH\",\"newPrice\":\"0.1\",\"token\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\"}',0,'/static/uploadQmPs8CApj3TQa3NNd2wnpZMYeuFPBpCyxRp2LGHn1hkQeU.jpeg','fwe',0,1639379000,1639379000);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (269,'3','9',58,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x80dd336deb7eea1923780a7383c806870c9ce27d','{\"tokenId\":4,\"from\":\"0x0000000000000000000000000000000000000000\",\"to\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"txHash\":\"0x0acb3ed73aa809d7fe08db9ede87cd8214885f19760be9a90c0c68271a2a8bb8\",\"token\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\"}',0,'/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg','12',0,1639379743,1639379743);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (270,'3','9',59,'0xc2c7c3ef46a07806c50a34a0c661cae55afde899','0xc2c7c3ef46a07806c50a34a0c661cae55afde899','{\"tokenId\":5,\"from\":\"0x0000000000000000000000000000000000000000\",\"to\":\"0xc2c7c3ef46a07806c50a34a0c661cae55afde899\",\"txHash\":\"0xeb7631164bc66fa5d304c25a68e8dd480f39262b341d665801cb81b48a54409a\",\"token\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\"}',0,'/static/uploadQmfPZzzQVsoTVrG8ixZQkZFLj84mFPi76sB7XwgoDcMxDV.png','bee',0,1639379983,1639379983);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (271,'3','9',58,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x80dd336deb7eea1923780a7383c806870c9ce27d','{\"tokenId\":6,\"from\":\"0x0000000000000000000000000000000000000000\",\"to\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"txHash\":\"0x1fbf061a0ba1b1c505686ef4f993fa7db08a9ebd5d1266b2d20345fb1230b4ae\",\"token\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\"}',0,'/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg','few',0,1639380283,1639380283);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (272,'3','5',58,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x65957894d458ab763fdf6242986c3388b4b7404b','{\"tokenId\":\"6\",\"biderAddr\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"uint\":\"WETH\",\"newPrice\":\"0.1\",\"token\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\"}',0,'/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg','few',0,1639380472,1639380472);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (273,'3','6',58,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x65957894d458ab763fdf6242986c3388b4b7404b','{\"tokenId\":\"6\",\"oldPrice\":\"0.1\",\"biderAddr\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"uint\":\"WETH\",\"newPrice\":\"0.2\",\"token\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\"}',0,'/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg','few',0,1639380478,1639380478);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (274,'3','8',60,'0x65957894d458ab763fdf6242986c3388b4b7404b','0x80dd336deb7eea1923780a7383c806870c9ce27d','{\"amount\":\"1\",\"quantity\":\"1\",\"tokenId\":\"6\",\"price\":\"1\",\"buyerAddr\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"sellerAddr\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"txHash\":\"0x691fe13e8f24cb1587d107c9caa39e0f1d284f6a59bd56cd1413c73312978d86\",\"priceUint\":\"WETH\",\"token\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\"}',0,'/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg','few',0,1639380553,1639380553);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (275,'3','18',58,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x80dd336deb7eea1923780a7383c806870c9ce27d','{\"amount\":\"1\",\"quantity\":\"1\",\"tokenId\":\"6\",\"price\":\"1\",\"buyerAddr\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"sellerAddr\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"txHash\":\"0x691fe13e8f24cb1587d107c9caa39e0f1d284f6a59bd56cd1413c73312978d86\",\"priceUint\":\"WETH\",\"token\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\"}',0,'/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg','few',0,1639380553,1639380553);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (276,'3','4',58,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x65957894d458ab763fdf6242986c3388b4b7404b','{\"amount\":\"1\",\"quantity\":\"1\",\"tokenId\":\"2\",\"price\":\"0.09\",\"buyerAddr\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"sellerAddr\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"txHash\":\"0x8a0868177fcbad71bae832b4ca623d41805a007e361da0848b45a5545a09fe68\",\"priceUint\":\"ETH\",\"token\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\"}',0,'/static/uploadQmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk/image.gif','fffe',0,1639380649,1639380649);
insert  into `fc_notice`(`id`,`type`,`sub_type`,`user_id`,`owner`,`operator`,`content`,`is_read`,`image`,`name`,`deleted`,`create_time`,`update_time`) values (277,'3','17',60,'0x65957894d458ab763fdf6242986c3388b4b7404b','0x65957894d458ab763fdf6242986c3388b4b7404b','{\"amount\":\"1\",\"quantity\":\"1\",\"tokenId\":\"2\",\"price\":\"0.09\",\"buyerAddr\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"sellerAddr\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"txHash\":\"0x8a0868177fcbad71bae832b4ca623d41805a007e361da0848b45a5545a09fe68\",\"priceUint\":\"ETH\",\"token\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\"}',0,'/static/uploadQmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk/image.gif','fffe',0,1639380649,1639380649);

/*Table structure for table `fc_order` */

DROP TABLE IF EXISTS `fc_order`;

CREATE TABLE `fc_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表ID',
  `owner` varchar(50) DEFAULT NULL COMMENT 'order发起者',
  `sell_token` varchar(50) DEFAULT NULL COMMENT '售卖token地址',
  `sell_token_id` varchar(256) DEFAULT NULL COMMENT '售卖token id',
  `sell_type` int(11) DEFAULT NULL COMMENT '售卖token类型',
  `sell_value` varchar(256) DEFAULT NULL COMMENT '售卖token价格',
  `buyer_token` varchar(50) DEFAULT NULL COMMENT '购买token地址',
  `buyer_token_id` varchar(256) DEFAULT NULL COMMENT '购买token id',
  `buyer_type` int(11) DEFAULT NULL COMMENT '购买token类型',
  `buyer_value` varchar(256) DEFAULT NULL COMMENT '购买token价格',
  `salt` varchar(100) DEFAULT NULL COMMENT '盐',
  `signature` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '签名',
  `status` int(11) DEFAULT '0' COMMENT '状态0 未完成， 1完成',
  `expired` tinyint(1) DEFAULT '0' COMMENT '是否过期',
  `order_type` int(11) DEFAULT NULL COMMENT '订单类型',
  `usdt_price` varchar(64) DEFAULT NULL COMMENT 'usdt价格',
  `sells` bigint(20) DEFAULT '0' COMMENT '已经售卖数量',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`owner`),
  KEY `index3` (`sell_token`),
  KEY `index4` (`sell_token_id`),
  KEY `index5` (`buyer_token`),
  KEY `index6` (`buyer_token_id`),
  KEY `index7` (`salt`),
  KEY `index8` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_order` */

insert  into `fc_order`(`id`,`owner`,`sell_token`,`sell_token_id`,`sell_type`,`sell_value`,`buyer_token`,`buyer_token_id`,`buyer_type`,`buyer_value`,`salt`,`signature`,`status`,`expired`,`order_type`,`usdt_price`,`sells`,`deleted`,`create_time`,`update_time`) values (56,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','1',3,'1','0xc778417e063141139fce010982780140aa0cd5ab','0',1,'1','61013491009332935296038377715606699094969035578940022317540874078411512988479','0xe4618fbc1b5116aaf6d18c99496badec7c0427d746fabb550cf2b827528924f870ef226dde729f3e476489fb91abbec059de9488fd6a9964e9e3f1100ab6d8131b',0,0,1,'100.00',0,0,1639378332,1639378332);
insert  into `fc_order`(`id`,`owner`,`sell_token`,`sell_token_id`,`sell_type`,`sell_value`,`buyer_token`,`buyer_token_id`,`buyer_type`,`buyer_value`,`salt`,`signature`,`status`,`expired`,`order_type`,`usdt_price`,`sells`,`deleted`,`create_time`,`update_time`) values (57,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','2',3,'1','0x0000000000000000000000000000000000000000','0',0,'0.09','13380475479165552914543896913925384938409931594905651063320532749764374971426','0xac289e39733560fd30f15c9eee23337071d73c96bfcc9ad212dec1c7a602b55d73e9846dbebb6ed7738b6bf85d290a934afe9fa83e660adaf73dd26bc6b559ca1c',1,1,1,'9.00',1,1,1639378543,1639380649);
insert  into `fc_order`(`id`,`owner`,`sell_token`,`sell_token_id`,`sell_type`,`sell_value`,`buyer_token`,`buyer_token_id`,`buyer_type`,`buyer_value`,`salt`,`signature`,`status`,`expired`,`order_type`,`usdt_price`,`sells`,`deleted`,`create_time`,`update_time`) values (58,'0x65957894d458ab763fdf6242986c3388b4b7404b','0xc778417e063141139fce010982780140aa0cd5ab','0',1,'0.1','0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','1',3,'1','46968051923765121270064389286436595958338416509158554426050030081894563272392','0xc713faefd86c336ddf127db0f827f285136d92cdb2dc6eedeb83e02fe19efcc83a5e291b75cc4af876691da4f3af865658eb2db562f757e03517ecc6b9f0ecf81c',0,0,2,'10.00',0,0,1639379000,1639379000);
insert  into `fc_order`(`id`,`owner`,`sell_token`,`sell_token_id`,`sell_type`,`sell_value`,`buyer_token`,`buyer_token_id`,`buyer_type`,`buyer_value`,`salt`,`signature`,`status`,`expired`,`order_type`,`usdt_price`,`sells`,`deleted`,`create_time`,`update_time`) values (59,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','4',3,'1','0xc778417e063141139fce010982780140aa0cd5ab','0',1,'0.1','19948298219163789199615828079985774715620661969636491380712331154377743535001','0x618384e1b8f352808a8508702e686dbbbd36c339a673fa6a8fc6631ef010b2ff27ea0d1ed4f777ba160d0a3c955d3554b2c9208430fd2dd135afd31b0ff1d3091b',0,0,1,'10.00',0,0,1639379821,1639379821);
insert  into `fc_order`(`id`,`owner`,`sell_token`,`sell_token_id`,`sell_type`,`sell_value`,`buyer_token`,`buyer_token_id`,`buyer_type`,`buyer_value`,`salt`,`signature`,`status`,`expired`,`order_type`,`usdt_price`,`sells`,`deleted`,`create_time`,`update_time`) values (60,'0xc2c7c3ef46a07806c50a34a0c661cae55afde899','0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','5',3,'1','0x0000000000000000000000000000000000000000','0',0,'0.3','63356904269639381345607650442395689378715090415787964155719801324856453453920','0x29346e8d3c5445176a7e826a1fb8f8125a372ba988495b26e3e35a72d367446773db2b9c0d4386534f0c8c65ab82c1c5af2e9f19f531b08046cdf2104cb56c9a1c',0,0,1,'30.00',0,0,1639379949,1639379949);
insert  into `fc_order`(`id`,`owner`,`sell_token`,`sell_token_id`,`sell_type`,`sell_value`,`buyer_token`,`buyer_token_id`,`buyer_type`,`buyer_value`,`salt`,`signature`,`status`,`expired`,`order_type`,`usdt_price`,`sells`,`deleted`,`create_time`,`update_time`) values (61,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','6',3,'1','0xc778417e063141139fce010982780140aa0cd5ab','0',1,'0.1','85539142819412538009758705263211709765951279634935070513741629103939047468251','0x83b814568266ce6ad56a2a6e3160855a0f4d89ca41383a5df3fe72342f62be6419956d35e7fa4f39da4e21fba85ad0c88e981f9902e75c45ee3f8ac19e79265a1c',0,0,1,'10.00',0,0,1639380449,1639380449);
insert  into `fc_order`(`id`,`owner`,`sell_token`,`sell_token_id`,`sell_type`,`sell_value`,`buyer_token`,`buyer_token_id`,`buyer_type`,`buyer_value`,`salt`,`signature`,`status`,`expired`,`order_type`,`usdt_price`,`sells`,`deleted`,`create_time`,`update_time`) values (62,'0x65957894d458ab763fdf6242986c3388b4b7404b','0xc778417e063141139fce010982780140aa0cd5ab','0',1,'0.2','0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','6',3,'1','37015275311913862070136063122484809325346560397728198055410305179614375132631','0x2f1ad6fa764341a7f41851ec77f629b974a1c071816ef67faaf2b4a3f563200574e8c25e1a3dab8579c353fd1745b0e03502a4a6f34f8aba6300d208ba0153fd1c',1,1,2,'20.00',1,1,1639380472,1639380553);

/*Table structure for table `fc_order_cache` */

DROP TABLE IF EXISTS `fc_order_cache`;

CREATE TABLE `fc_order_cache` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `value` varchar(1024) DEFAULT NULL,
  `expired_time` bigint(20) DEFAULT '0',
  `deleted` tinyint(1) DEFAULT '0',
  `create_time` bigint(20) DEFAULT '0',
  `update_time` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_order_cache` */

insert  into `fc_order_cache`(`id`,`key`,`value`,`expired_time`,`deleted`,`create_time`,`update_time`) values (2,'0x80dd336deb7eea1923780a7383c806870c9ce27d0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb02','{\"buyToken\":\"0x0000000000000000000000000000000000000000\",\"buyTokenId\":\"0\",\"buyType\":\"0\",\"buyValue\":\"90000000000000000\",\"message\":\"0x2f24fe220ad6c9a102e3d64e11195cf0fe83d9fc712cef123acfed8d8eb0458a\",\"owner\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"salt\":\"13380475479165552914543896913925384938409931594905651063320532749764374971426\",\"sellFee\":\"250\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"2\",\"sellType\":\"3\",\"sellValue\":\"1\",\"type\":1}',1639382364764,0,1639378542,1639378542);
insert  into `fc_order_cache`(`id`,`key`,`value`,`expired_time`,`deleted`,`create_time`,`update_time`) values (3,'0x65957894d458ab763fdf6242986c3388b4b7404b0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb01','{\"buyToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"buyTokenId\":\"1\",\"buyType\":\"3\",\"buyValue\":\"1\",\"message\":\"0xc1c3d1c527b8195f7feb2301e3a115bb0edbe89ff6a28165e2556f4a014d78c8\",\"owner\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"salt\":\"46968051923765121270064389286436595958338416509158554426050030081894563272392\",\"sellFee\":\"250\",\"sellToken\":\"0xc778417e063141139fce010982780140aa0cd5ab\",\"sellTokenId\":\"0\",\"sellType\":\"1\",\"sellValue\":\"100000000000000000\",\"type\":5}',1639380798952,0,1639378998,1639378998);
insert  into `fc_order_cache`(`id`,`key`,`value`,`expired_time`,`deleted`,`create_time`,`update_time`) values (9,'0x80dd336deb7eea1923780a7383c806870c9ce27d0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb04','{\"buyToken\":\"0xc778417e063141139fce010982780140aa0cd5ab\",\"buyTokenId\":\"0\",\"buyType\":\"1\",\"buyValue\":\"90000000000000000\",\"message\":\"0x0be87887ad40149d631efee4b795b7f0665ff5645e827c9411a37810035fa9de\",\"owner\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"salt\":\"19948298219163789199615828079985774715620661969636491380712331154377743535001\",\"sellFee\":\"250\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"4\",\"sellType\":\"3\",\"sellValue\":\"1\",\"type\":1}',1639381649500,0,1639379849,1639379849);
insert  into `fc_order_cache`(`id`,`key`,`value`,`expired_time`,`deleted`,`create_time`,`update_time`) values (11,'0xc2c7c3ef46a07806c50a34a0c661cae55afde8990x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb05','{\"buyToken\":\"0x0000000000000000000000000000000000000000\",\"buyTokenId\":\"0\",\"buyType\":\"0\",\"buyValue\":\"300000000000000000\",\"message\":\"0x3f51c184e1b7d5c2edc801e8814367c4fcc7eac0f0b3bb73b85b0cb473edaca9\",\"owner\":\"0xc2c7c3ef46a07806c50a34a0c661cae55afde899\",\"salt\":\"63356904269639381345607650442395689378715090415787964155719801324856453453920\",\"sellFee\":\"250\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"5\",\"sellType\":\"3\",\"sellValue\":\"1\",\"type\":1}',1639382337167,0,1639380001,1639380001);
insert  into `fc_order_cache`(`id`,`key`,`value`,`expired_time`,`deleted`,`create_time`,`update_time`) values (13,'0x65957894d458ab763fdf6242986c3388b4b7404b0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb06','{\"buyToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"buyTokenId\":\"6\",\"buyType\":\"3\",\"buyValue\":\"1\",\"message\":\"0x9d15ef27b25c934d24d3f8ccd3cb78aa2043ea72746168bb3a7f5dc9f59fadb4\",\"owner\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"salt\":\"37015275311913862070136063122484809325346560397728198055410305179614375132631\",\"sellFee\":\"250\",\"sellToken\":\"0xc778417e063141139fce010982780140aa0cd5ab\",\"sellTokenId\":\"0\",\"sellType\":\"1\",\"sellValue\":\"200000000000000000\",\"type\":5}',1639382276801,0,1639380470,1639380470);
insert  into `fc_order_cache`(`id`,`key`,`value`,`expired_time`,`deleted`,`create_time`,`update_time`) values (14,'0x65957894d458ab763fdf6242986c3388b4b7404b','{\"buyFee\":\"250\",\"buyToken\":\"0x0000000000000000000000000000000000000000\",\"buyTokenId\":\"0\",\"buyType\":\"0\",\"buyValue\":\"90000000000000000\",\"owner\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"r\":\"0x4acc471f3d50c510793a504af61d934fbe2551b017003031e64bf3c1bbe2f717\",\"s\":\"0x3d9fe600ae3a228d0556025319d31a3aed5c9e35c6220e1ff8a4823dbb020aef\",\"salt\":\"13380475479165552914543896913925384938409931594905651063320532749764374971426\",\"sellFee\":\"250\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"2\",\"sellType\":\"3\",\"sellValue\":\"1\",\"v\":\"28\"}',1639382391524,0,1639380491,1639380491);

/*Table structure for table `fc_order_log` */

DROP TABLE IF EXISTS `fc_order_log`;

CREATE TABLE `fc_order_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
  `from` varchar(50) DEFAULT NULL COMMENT '订单发起人地址',
  `to` varchar(50) DEFAULT NULL COMMENT '交易对象地址',
  `type` int(11) DEFAULT NULL COMMENT '交易类型sale_cancel, sale_buy, bid_cancel, bid_accept, erc721_create, erc1155_create, token_tranfer, token_burn',
  `tx_hash` varchar(100) DEFAULT NULL COMMENT '交易hash',
  `pre_log_id` bigint(20) DEFAULT NULL COMMENT '前一个订单日志id',
  `token` varchar(50) DEFAULT NULL COMMENT '操作NFT的地址',
  `token_id` varchar(256) DEFAULT NULL COMMENT '操作NFT的tokenId',
  `content` mediumtext COMMENT '日志体',
  `expired` tinyint(1) DEFAULT '0' COMMENT '是否过期',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`order_id`),
  KEY `index3` (`type`),
  KEY `index4` (`token`),
  KEY `index5` (`token_id`)
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_order_log` */

insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (218,NULL,'0x0000000000000000000000000000000000000000','0x80dd336deb7eea1923780a7383c806870c9ce27d',9,'0x41febf6832f77a9d22aca14120a903f7f83327d7991bdef04d7279d9414d7461',0,'0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0','1','{\"address\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\",\"blockNumber\":9806232,\"indexedValues\":[{\"typeAsString\":\"address\",\"value\":\"0x0000000000000000000000000000000000000000\"},{\"typeAsString\":\"address\",\"value\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\"},{\"bitSize\":256,\"typeAsString\":\"uint256\",\"value\":1}],\"nonIndexedValues\":[],\"timestamp\":1639378254,\"txHash\":\"0x41febf6832f77a9d22aca14120a903f7f83327d7991bdef04d7279d9414d7461\"}',0,0,1639378303,1639378303);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (219,56,'0x80dd336deb7eea1923780a7383c806870c9ce27d','',1,'',0,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','1','{\"buyerToken\":\"0xc778417e063141139fce010982780140aa0cd5ab\",\"buyerTokenId\":\"0\",\"buyerType\":1,\"buyerValue\":\"1\",\"createTime\":1639378332,\"deleted\":false,\"expired\":false,\"id\":56,\"orderType\":1,\"owner\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"salt\":\"61013491009332935296038377715606699094969035578940022317540874078411512988479\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"1\",\"sellType\":3,\"sellValue\":\"1\",\"signature\":\"0xe4618fbc1b5116aaf6d18c99496badec7c0427d746fabb550cf2b827528924f870ef226dde729f3e476489fb91abbec059de9488fd6a9964e9e3f1100ab6d8131b\",\"status\":0,\"updateTime\":1639378332,\"usdtPrice\":\"100.00\"}',0,0,1639378332,1639378332);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (220,57,'0x80dd336deb7eea1923780a7383c806870c9ce27d','',1,'',0,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','2','{\"buyerToken\":\"0x0000000000000000000000000000000000000000\",\"buyerTokenId\":\"0\",\"buyerType\":0,\"buyerValue\":\"0.1\",\"createTime\":1639378543,\"deleted\":false,\"expired\":false,\"id\":57,\"orderType\":1,\"owner\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"salt\":\"13380475479165552914543896913925384938409931594905651063320532749764374971426\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"2\",\"sellType\":3,\"sellValue\":\"1\",\"signature\":\"0xb03afbffb563d67b705ca4f042d5e4d47bb256ae3dcd380ef0a64487b273f9fb7f9fc41fae2374e08b14ea28d2a8b4c32230a513c9901ed7f04d70c523b7c2e31c\",\"status\":0,\"updateTime\":1639378543,\"usdtPrice\":\"10.00\"}',1,0,1639378543,1639378543);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (221,NULL,'0x0000000000000000000000000000000000000000','0x80dd336deb7eea1923780a7383c806870c9ce27d',9,'0x16452a98044f770340040b01f7adfb85e891e7946f8a7813913fb2026d9bdecb',0,'0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0','2','{\"address\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\",\"blockNumber\":9806250,\"indexedValues\":[{\"typeAsString\":\"address\",\"value\":\"0x0000000000000000000000000000000000000000\"},{\"typeAsString\":\"address\",\"value\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\"},{\"bitSize\":256,\"typeAsString\":\"uint256\",\"value\":2}],\"nonIndexedValues\":[],\"timestamp\":1639378524,\"txHash\":\"0x16452a98044f770340040b01f7adfb85e891e7946f8a7813913fb2026d9bdecb\"}',0,0,1639378573,1639378573);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (222,NULL,'0x0000000000000000000000000000000000000000','0x80dd336deb7eea1923780a7383c806870c9ce27d',9,'0x237065a3cc5f05d6ca8fbfdc915a7f913af0b2469045eb19c6aa55141fac88e8',0,'0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0','3','{\"address\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\",\"blockNumber\":9806254,\"indexedValues\":[{\"typeAsString\":\"address\",\"value\":\"0x0000000000000000000000000000000000000000\"},{\"typeAsString\":\"address\",\"value\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\"},{\"bitSize\":256,\"typeAsString\":\"uint256\",\"value\":3}],\"nonIndexedValues\":[],\"timestamp\":1639378584,\"txHash\":\"0x237065a3cc5f05d6ca8fbfdc915a7f913af0b2469045eb19c6aa55141fac88e8\"}',0,0,1639378633,1639378633);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (223,58,'0x65957894d458ab763fdf6242986c3388b4b7404b','',5,'',0,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','1','{\"buyerToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"buyerTokenId\":\"1\",\"buyerType\":3,\"buyerValue\":\"1\",\"createTime\":1639379000,\"deleted\":false,\"expired\":false,\"id\":58,\"orderType\":2,\"owner\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"salt\":\"46968051923765121270064389286436595958338416509158554426050030081894563272392\",\"sellToken\":\"0xc778417e063141139fce010982780140aa0cd5ab\",\"sellTokenId\":\"0\",\"sellType\":1,\"sellValue\":\"0.1\",\"sells\":0,\"signature\":\"0xc713faefd86c336ddf127db0f827f285136d92cdb2dc6eedeb83e02fe19efcc83a5e291b75cc4af876691da4f3af865658eb2db562f757e03517ecc6b9f0ecf81c\",\"status\":0,\"updateTime\":1639379000,\"usdtPrice\":\"10.00\"}',0,0,1639379000,1639379000);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (224,NULL,'0x0000000000000000000000000000000000000000','0x80dd336deb7eea1923780a7383c806870c9ce27d',9,'0x0acb3ed73aa809d7fe08db9ede87cd8214885f19760be9a90c0c68271a2a8bb8',0,'0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0','4','{\"address\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\",\"blockNumber\":9806329,\"indexedValues\":[{\"typeAsString\":\"address\",\"value\":\"0x0000000000000000000000000000000000000000\"},{\"typeAsString\":\"address\",\"value\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\"},{\"bitSize\":256,\"typeAsString\":\"uint256\",\"value\":4}],\"nonIndexedValues\":[],\"timestamp\":1639379710,\"txHash\":\"0x0acb3ed73aa809d7fe08db9ede87cd8214885f19760be9a90c0c68271a2a8bb8\"}',0,0,1639379743,1639379743);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (225,59,'0x80dd336deb7eea1923780a7383c806870c9ce27d','',1,'',0,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','4','{\"buyerToken\":\"0xc778417e063141139fce010982780140aa0cd5ab\",\"buyerTokenId\":\"0\",\"buyerType\":1,\"buyerValue\":\"0.1\",\"createTime\":1639379821,\"deleted\":false,\"expired\":false,\"id\":59,\"orderType\":1,\"owner\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"salt\":\"19948298219163789199615828079985774715620661969636491380712331154377743535001\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"4\",\"sellType\":3,\"sellValue\":\"1\",\"signature\":\"0x618384e1b8f352808a8508702e686dbbbd36c339a673fa6a8fc6631ef010b2ff27ea0d1ed4f777ba160d0a3c955d3554b2c9208430fd2dd135afd31b0ff1d3091b\",\"status\":0,\"updateTime\":1639379821,\"usdtPrice\":\"10.00\"}',0,0,1639379821,1639379821);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (226,60,'0xc2c7c3ef46a07806c50a34a0c661cae55afde899','',1,'',0,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','5','{\"buyerToken\":\"0x0000000000000000000000000000000000000000\",\"buyerTokenId\":\"0\",\"buyerType\":0,\"buyerValue\":\"0.5\",\"createTime\":1639379949,\"deleted\":false,\"expired\":false,\"id\":60,\"orderType\":1,\"owner\":\"0xc2c7c3ef46a07806c50a34a0c661cae55afde899\",\"salt\":\"63356904269639381345607650442395689378715090415787964155719801324856453453920\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"5\",\"sellType\":3,\"sellValue\":\"1\",\"signature\":\"0x1ae744aa90f32cd83e798aca9eaf0f35734865435723ca411e384adf623d43375860addae6852fe70f4390a04b75fb26bd3b4ca465ebb6e5b0d46eb616035ee21c\",\"status\":0,\"updateTime\":1639379949,\"usdtPrice\":\"50.00\"}',1,0,1639379949,1639379949);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (227,NULL,'0x0000000000000000000000000000000000000000','0xc2c7c3ef46a07806c50a34a0c661cae55afde899',9,'0xeb7631164bc66fa5d304c25a68e8dd480f39262b341d665801cb81b48a54409a',0,'0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0','5','{\"address\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\",\"blockNumber\":9806344,\"indexedValues\":[{\"typeAsString\":\"address\",\"value\":\"0x0000000000000000000000000000000000000000\"},{\"typeAsString\":\"address\",\"value\":\"0xc2c7c3ef46a07806c50a34a0c661cae55afde899\"},{\"bitSize\":256,\"typeAsString\":\"uint256\",\"value\":5}],\"nonIndexedValues\":[],\"timestamp\":1639379935,\"txHash\":\"0xeb7631164bc66fa5d304c25a68e8dd480f39262b341d665801cb81b48a54409a\"}',0,0,1639379983,1639379983);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (228,NULL,'0x0000000000000000000000000000000000000000','0x80dd336deb7eea1923780a7383c806870c9ce27d',9,'0x1fbf061a0ba1b1c505686ef4f993fa7db08a9ebd5d1266b2d20345fb1230b4ae',0,'0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0','6','{\"address\":\"0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0\",\"blockNumber\":9806364,\"indexedValues\":[{\"typeAsString\":\"address\",\"value\":\"0x0000000000000000000000000000000000000000\"},{\"typeAsString\":\"address\",\"value\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\"},{\"bitSize\":256,\"typeAsString\":\"uint256\",\"value\":6}],\"nonIndexedValues\":[],\"timestamp\":1639380236,\"txHash\":\"0x1fbf061a0ba1b1c505686ef4f993fa7db08a9ebd5d1266b2d20345fb1230b4ae\"}',0,0,1639380283,1639380283);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (229,61,'0x80dd336deb7eea1923780a7383c806870c9ce27d','',1,'',0,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','6','{\"buyerToken\":\"0xc778417e063141139fce010982780140aa0cd5ab\",\"buyerTokenId\":\"0\",\"buyerType\":1,\"buyerValue\":\"0.1\",\"createTime\":1639380449,\"deleted\":false,\"expired\":false,\"id\":61,\"orderType\":1,\"owner\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"salt\":\"85539142819412538009758705263211709765951279634935070513741629103939047468251\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"6\",\"sellType\":3,\"sellValue\":\"1\",\"signature\":\"0x83b814568266ce6ad56a2a6e3160855a0f4d89ca41383a5df3fe72342f62be6419956d35e7fa4f39da4e21fba85ad0c88e981f9902e75c45ee3f8ac19e79265a1c\",\"status\":0,\"updateTime\":1639380449,\"usdtPrice\":\"10.00\"}',0,0,1639380449,1639380449);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (230,62,'0x65957894d458ab763fdf6242986c3388b4b7404b','',5,'',0,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','6','{\"buyerToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"buyerTokenId\":\"6\",\"buyerType\":3,\"buyerValue\":\"1\",\"createTime\":1639380472,\"deleted\":false,\"expired\":false,\"id\":62,\"orderType\":2,\"owner\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"salt\":\"37015275311913862070136063122484809325346560397728198055410305179614375132631\",\"sellToken\":\"0xc778417e063141139fce010982780140aa0cd5ab\",\"sellTokenId\":\"0\",\"sellType\":1,\"sellValue\":\"0.1\",\"sells\":0,\"signature\":\"0x42a43c96ab1e17cafeb06294639e52b5787edbff9e8b485dc09d9988c267de5d2e1e2f25ea1c702432406e52e81361ecad41b2a36b1dadfc7d91d7e5a5d04e591b\",\"status\":0,\"updateTime\":1639380472,\"usdtPrice\":\"10.00\"}',1,0,1639380472,1639380472);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (231,62,'0x65957894d458ab763fdf6242986c3388b4b7404b','',6,'',230,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','6','{\"buyerToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"buyerTokenId\":\"6\",\"buyerType\":3,\"buyerValue\":\"1\",\"createTime\":1639380472,\"deleted\":false,\"expired\":false,\"id\":62,\"orderType\":2,\"owner\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"salt\":\"37015275311913862070136063122484809325346560397728198055410305179614375132631\",\"sellToken\":\"0xc778417e063141139fce010982780140aa0cd5ab\",\"sellTokenId\":\"0\",\"sellType\":1,\"sellValue\":\"0.2\",\"sells\":0,\"signature\":\"0x2f1ad6fa764341a7f41851ec77f629b974a1c071816ef67faaf2b4a3f563200574e8c25e1a3dab8579c353fd1745b0e03502a4a6f34f8aba6300d208ba0153fd1c\",\"status\":0,\"updateTime\":1639380472,\"usdtPrice\":\"20.00\"}',1,0,1639380478,1639380478);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (232,60,'0xc2c7c3ef46a07806c50a34a0c661cae55afde899','',2,'',226,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','5','{\"buyerToken\":\"0x0000000000000000000000000000000000000000\",\"buyerTokenId\":\"0\",\"buyerType\":0,\"buyerValue\":\"0.4\",\"createTime\":1639379949,\"deleted\":false,\"expired\":false,\"id\":60,\"orderType\":1,\"owner\":\"0xc2c7c3ef46a07806c50a34a0c661cae55afde899\",\"salt\":\"63356904269639381345607650442395689378715090415787964155719801324856453453920\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"5\",\"sellType\":3,\"sellValue\":\"1\",\"sells\":0,\"signature\":\"0x6719d7d7f8cc66134f37128ee26ec9e42c8325ce11e9b96c7491e705bbfb6007643e1f5e4071141349cbf6c7ef0b15d7269dae88ff30e49c03450acd4e186ff41b\",\"status\":0,\"updateTime\":1639379949,\"usdtPrice\":\"40.00\"}',1,0,1639380519,1639380519);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (233,60,'0xc2c7c3ef46a07806c50a34a0c661cae55afde899','',2,'',232,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','5','{\"buyerToken\":\"0x0000000000000000000000000000000000000000\",\"buyerTokenId\":\"0\",\"buyerType\":0,\"buyerValue\":\"0.3\",\"createTime\":1639379949,\"deleted\":false,\"expired\":false,\"id\":60,\"orderType\":1,\"owner\":\"0xc2c7c3ef46a07806c50a34a0c661cae55afde899\",\"salt\":\"63356904269639381345607650442395689378715090415787964155719801324856453453920\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"5\",\"sellType\":3,\"sellValue\":\"1\",\"sells\":0,\"signature\":\"0x29346e8d3c5445176a7e826a1fb8f8125a372ba988495b26e3e35a72d367446773db2b9c0d4386534f0c8c65ab82c1c5af2e9f19f531b08046cdf2104cb56c9a1c\",\"status\":0,\"updateTime\":1639379949,\"usdtPrice\":\"30.00\"}',0,0,1639380539,1639380539);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (234,62,'0x65957894d458ab763fdf6242986c3388b4b7404b','0x80dd336deb7eea1923780a7383c806870c9ce27d',8,'0x691fe13e8f24cb1587d107c9caa39e0f1d284f6a59bd56cd1413c73312978d86',0,'0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0','6','{\"buyerToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"buyerTokenId\":\"6\",\"buyerType\":3,\"buyerValue\":\"1\",\"createTime\":1639380472,\"deleted\":false,\"expired\":false,\"id\":62,\"orderType\":2,\"owner\":\"0x65957894d458ab763fdf6242986c3388b4b7404b\",\"salt\":\"37015275311913862070136063122484809325346560397728198055410305179614375132631\",\"sellToken\":\"0xc778417e063141139fce010982780140aa0cd5ab\",\"sellTokenId\":\"0\",\"sellType\":1,\"sellValue\":\"0.2\",\"sells\":1,\"signature\":\"0x2f1ad6fa764341a7f41851ec77f629b974a1c071816ef67faaf2b4a3f563200574e8c25e1a3dab8579c353fd1745b0e03502a4a6f34f8aba6300d208ba0153fd1c\",\"status\":0,\"updateTime\":1639380472,\"usdtPrice\":\"20.00\"}',0,0,1639380553,1639380553);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (235,57,'0x80dd336deb7eea1923780a7383c806870c9ce27d','',2,'',220,'0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0','2','{\"buyerToken\":\"0x0000000000000000000000000000000000000000\",\"buyerTokenId\":\"0\",\"buyerType\":0,\"buyerValue\":\"0.09\",\"createTime\":1639378543,\"deleted\":false,\"expired\":false,\"id\":57,\"orderType\":1,\"owner\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"salt\":\"13380475479165552914543896913925384938409931594905651063320532749764374971426\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"2\",\"sellType\":3,\"sellValue\":\"1\",\"sells\":0,\"signature\":\"0xac289e39733560fd30f15c9eee23337071d73c96bfcc9ad212dec1c7a602b55d73e9846dbebb6ed7738b6bf85d290a934afe9fa83e660adaf73dd26bc6b559ca1c\",\"status\":0,\"updateTime\":1639378543,\"usdtPrice\":\"9.00\"}',0,0,1639380566,1639380566);
insert  into `fc_order_log`(`id`,`order_id`,`from`,`to`,`type`,`tx_hash`,`pre_log_id`,`token`,`token_id`,`content`,`expired`,`deleted`,`create_time`,`update_time`) values (236,57,'0x80dd336deb7eea1923780a7383c806870c9ce27d','0x65957894d458ab763fdf6242986c3388b4b7404b',4,'0x8a0868177fcbad71bae832b4ca623d41805a007e361da0848b45a5545a09fe68',0,'0x5377f6a0e8fae84e68a0af6de8d3eb43556e8fb0','2','{\"buyerToken\":\"0x0000000000000000000000000000000000000000\",\"buyerTokenId\":\"0\",\"buyerType\":0,\"buyerValue\":\"0.09\",\"createTime\":1639378543,\"deleted\":false,\"expired\":false,\"id\":57,\"orderType\":1,\"owner\":\"0x80dd336deb7eea1923780a7383c806870c9ce27d\",\"salt\":\"13380475479165552914543896913925384938409931594905651063320532749764374971426\",\"sellToken\":\"0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0\",\"sellTokenId\":\"2\",\"sellType\":3,\"sellValue\":\"1\",\"sells\":1,\"signature\":\"0xac289e39733560fd30f15c9eee23337071d73c96bfcc9ad212dec1c7a602b55d73e9846dbebb6ed7738b6bf85d290a934afe9fa83e660adaf73dd26bc6b559ca1c\",\"status\":0,\"updateTime\":1639378543,\"usdtPrice\":\"9.00\"}',0,0,1639380649,1639380649);

/*Table structure for table `fc_pay_token` */

DROP TABLE IF EXISTS `fc_pay_token`;

CREATE TABLE `fc_pay_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `name` varchar(100) NOT NULL COMMENT 'token名称',
  `symbol` varchar(100) NOT NULL COMMENT 'token symbol',
  `decimals` int(11) NOT NULL COMMENT 'token 精度',
  `rate` decimal(24,16) DEFAULT '1.0000000000000000' COMMENT 'token 汇率',
  `address` varchar(50) DEFAULT NULL COMMENT 'token 合约地址',
  `img_url` varchar(512) DEFAULT NULL COMMENT 'token 图像',
  `storage_id` bigint(20) DEFAULT NULL COMMENT 'token 图像保存id',
  `type` int(11) DEFAULT NULL COMMENT '0 ETH 1 WETH 2 ERC1155 3 ERC721 4 ERC721Deprecated',
  `is_default` tinyint(4) NOT NULL DEFAULT '0' COMMENT '默认支付类型',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_pay_token` */

insert  into `fc_pay_token`(`id`,`name`,`symbol`,`decimals`,`rate`,`address`,`img_url`,`storage_id`,`type`,`is_default`,`deleted`,`create_time`,`update_time`) values (63,'ETH','ETH',18,1.0000000000000000,'0x0000000000000000000000000000000000000000',NULL,NULL,0,0,0,1639366025,1639366025);
insert  into `fc_pay_token`(`id`,`name`,`symbol`,`decimals`,`rate`,`address`,`img_url`,`storage_id`,`type`,`is_default`,`deleted`,`create_time`,`update_time`) values (64,'WETH','WETH',18,1.0000000000000000,'0xc778417e063141139fce010982780140aa0cd5ab',NULL,NULL,1,1,0,1639366213,1639366213);
insert  into `fc_pay_token`(`id`,`name`,`symbol`,`decimals`,`rate`,`address`,`img_url`,`storage_id`,`type`,`is_default`,`deleted`,`create_time`,`update_time`) values (65,'USDT','USDT',18,1.0000000000000000,'0xD9BA894E0097f8cC2BBc9D24D308b98e36dc6D02',NULL,NULL,1,0,0,1639366260,1639366266);

/*Table structure for table `fc_storage` */

DROP TABLE IF EXISTS `fc_storage`;

CREATE TABLE `fc_storage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key` varchar(63) NOT NULL COMMENT '文件的唯一索引',
  `name` varchar(255) NOT NULL COMMENT '文件名',
  `type` varchar(20) NOT NULL COMMENT '文件类型',
  `size` int(11) NOT NULL COMMENT '文件大小',
  `url` varchar(255) DEFAULT NULL COMMENT '文件访问链接',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `ipfsHash` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=263 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件存储表';

/*Data for the table `fc_storage` */

insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (249,'6q5heqodtvf4wedazl4v.png','6@2x.png','image/png',218171,'/static/uploadQmfPZzzQVsoTVrG8ixZQkZFLj84mFPi76sB7XwgoDcMxDV.png',1639378198,1639378198,0,'ipfs://ipfs/QmfPZzzQVsoTVrG8ixZQkZFLj84mFPi76sB7XwgoDcMxDV');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (250,'r4q0oov58114v85c7ah7.jpeg','6.jpeg','image/jpeg',86953,'/static/uploadQmPs8CApj3TQa3NNd2wnpZMYeuFPBpCyxRp2LGHn1hkQeU.jpeg',1639378241,1639378241,0,'ipfs://ipfs/QmPs8CApj3TQa3NNd2wnpZMYeuFPBpCyxRp2LGHn1hkQeU');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (251,'13riuwxtcqrhwd036uvf.json','fwe.json','application/json',229,'/static/uploadQmeqNkbcMVBoH7xYpPwMgLrUL9TyLejUWNM5gexxJEbNdA.json',1639378246,1639378246,0,'ipfs://ipfs/QmeqNkbcMVBoH7xYpPwMgLrUL9TyLejUWNM5gexxJEbNdA');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (252,'animation.mp4','15.mp4','video/mp4',487604,'/static/uploadQmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk/animation.mp4',1639378503,1639378503,0,'ipfs://ipfs/QmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (253,'image.gif','15.gif','image/gif',1246183,'/static/uploadQmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk/image.gif',1639378503,1639378503,0,'ipfs://ipfs/QmReaLdC2F8kHFgPEKoG73n59oMdhdis2UPzcpge15ergk');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (254,'whqe5o2mv5t9a7xl4jgu.json','fffe.json','application/json',312,'/static/uploadQmamKwTwDmMPp6bZgh3CJADP5XPzux1XMWBNcDKfQeZR4v.json',1639378505,1639378505,0,'ipfs://ipfs/QmamKwTwDmMPp6bZgh3CJADP5XPzux1XMWBNcDKfQeZR4v');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (255,'z0nn5oyabfznn58uavmk.jpeg','4.jpeg','image/jpeg',150405,'/static/uploadQmYJEdDX1TV2dW7apj7sU3Rw4dPfNCdf9c6sw3Bhej9S1d.jpeg',1639378564,1639378564,0,'ipfs://ipfs/QmYJEdDX1TV2dW7apj7sU3Rw4dPfNCdf9c6sw3Bhej9S1d');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (256,'l1eer5u617b9qo1nnl9a.json','fwe.json','application/json',238,'/static/uploadQmUE3mbaGk7QobsvN6z5hM2DBz7JJsoWytWeW7FHCRgAeQ.json',1639378565,1639378565,0,'ipfs://ipfs/QmUE3mbaGk7QobsvN6z5hM2DBz7JJsoWytWeW7FHCRgAeQ');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (257,'3b9ckkcy7r8eq13wz72y.jpeg','3.jpeg','image/jpeg',167135,'/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg',1639379691,1639379691,0,'ipfs://ipfs/QmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (258,'we9jaurvpbpd0099n759.json','12.json','application/json',228,'/static/uploadQmZTQu4QyekV3282naCbaM1H8gfGV36Tn9uRHhMfCPeqLH.json',1639379693,1639379693,0,'ipfs://ipfs/QmZTQu4QyekV3282naCbaM1H8gfGV36Tn9uRHhMfCPeqLH');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (259,'ihptyegiujuuiah8x101.png','6@2x.png','image/png',218171,'/static/uploadQmfPZzzQVsoTVrG8ixZQkZFLj84mFPi76sB7XwgoDcMxDV.png',1639379915,1639379915,0,'ipfs://ipfs/QmfPZzzQVsoTVrG8ixZQkZFLj84mFPi76sB7XwgoDcMxDV');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (260,'afoz8s4t65qzcmzmmchs.json','bee.json','application/json',278,'/static/uploadQmQPSP9CFFdzPgUyiL6LrESBt6LXAFkG37S6dJA5V9mUJu.json',1639379917,1639379917,0,'ipfs://ipfs/QmQPSP9CFFdzPgUyiL6LrESBt6LXAFkG37S6dJA5V9mUJu');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (261,'aqtjxj82uo6qzvwc0in0.jpeg','3.jpeg','image/jpeg',167135,'/static/uploadQmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj.jpeg',1639380225,1639380225,0,'ipfs://ipfs/QmaLL1wXYkKSe1ZTw4cEvcoXDiejaLGpknrshVQ7jSgrqj');
insert  into `fc_storage`(`id`,`key`,`name`,`type`,`size`,`url`,`create_time`,`update_time`,`deleted`,`ipfsHash`) values (262,'klmk2ywuzunr9n5vzjq4.json','few.json','application/json',275,'/static/uploadQmQhLXvBH5RYG5J6xL6Uq1YDefhncor7fXtPzhnNpsVTag.json',1639380226,1639380226,0,'ipfs://ipfs/QmQhLXvBH5RYG5J6xL6Uq1YDefhncor7fXtPzhnNpsVTag');

/*Table structure for table `fc_system` */

DROP TABLE IF EXISTS `fc_system`;

CREATE TABLE `fc_system` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(255) NOT NULL COMMENT '系统配置名',
  `key_value` varchar(255) NOT NULL COMMENT '系统配置值',
  `show` tinyint(1) DEFAULT '1' COMMENT '是否在前台显示',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index2` (`key_name`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';

/*Data for the table `fc_system` */

insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (1,'networkId','4',1,1619549758,1639365653,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (2,'ipfsUrl','https://ipfs.fingerchar.com',1,1619549758,1639378471,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (3,'sellerFee','250',1,1619549758,1639365661,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (4,'buyerFee','250',1,1619549758,1639365666,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (5,'NFT721','0x5377F6a0e8fAe84e68a0Af6de8d3EB43556e8fb0',1,1619549758,1639367012,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (6,'TransferProxy','0xA97e2C8725E42DDab31Af8e5FAF046E4A8dB4f66',1,1619549758,1639365699,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (7,'TransferProxyForDeprecated','0xB8D06ABBea832efa937f355060C8296FCc079858',1,1619549758,1639365705,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (8,'ERC20TransferProxy','0x9F5b8d15cC817ca8cA778b399f2fE2B1e7a202b3',1,1619549758,1639365711,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (9,'NftExchange','0x680Ff2011292A9D8B160B7Ec2DaAd549e1C77fC5',1,1619549758,1639379556,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (10,'StaticPath','https://fingernft.fingerchar.com',1,1619549758,1639365588,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (11,'LastBlock','9806419',0,1619549758,1639381092,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (12,'NftDefaultVerify','1',0,1619549758,1639367101,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (13,'LoginMessage','Welcome. Login Finger NFT Market. This is completely secure and doesn\'t cost anything!',1,1619549758,1639365614,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (14,'WebSite','https://fingernft.fingerchar.com',1,1619549758,1639365544,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (15,'ChainApiUrl','https://rinkeby-light.eth.linkpool.io/',0,1625305504,1639365649,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (16,'IpfsServerIp','43.128.40.5',0,1625305504,1639377765,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (17,'IpfsServerPort','20002',0,1625305504,1639378180,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (18,'IpfsRemoteServer','',0,1625305504,1639367121,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (19,'MintKey','90ee1d135226d036df9582fb570281cf0a6f8421ea881ca5877124bdbd9eb41a',0,1625305504,1639365927,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (20,'TransKey','a81d497b1dc1d91b0c5b326ee546e85dacdc75e64ecbe429643d3a7ad4512285',0,1625305504,1639365935,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (21,'StorageLocalPath','static/upload',0,1625305504,1625305504,0);
insert  into `fc_system`(`id`,`key_name`,`key_value`,`show`,`create_time`,`update_time`,`deleted`) values (22,'StorageRequestBase','/static/upload',0,1625305504,1625305504,0);

/*Table structure for table `fc_tx_order` */

DROP TABLE IF EXISTS `fc_tx_order`;

CREATE TABLE `fc_tx_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `tx_hash` varchar(100) DEFAULT NULL COMMENT '交易hash',
  `is_sync` tinyint(1) DEFAULT '0' COMMENT '是否已经同步',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index2` (`tx_hash`)
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_tx_order` */

insert  into `fc_tx_order`(`id`,`tx_hash`,`is_sync`,`deleted`,`create_time`,`update_time`) values (162,'0x41febf6832f77a9d22aca14120a903f7f83327d7991bdef04d7279d9414d7461',1,0,1639378303,1639378303);
insert  into `fc_tx_order`(`id`,`tx_hash`,`is_sync`,`deleted`,`create_time`,`update_time`) values (163,'0x16452a98044f770340040b01f7adfb85e891e7946f8a7813913fb2026d9bdecb',1,0,1639378573,1639378573);
insert  into `fc_tx_order`(`id`,`tx_hash`,`is_sync`,`deleted`,`create_time`,`update_time`) values (164,'0x237065a3cc5f05d6ca8fbfdc915a7f913af0b2469045eb19c6aa55141fac88e8',1,0,1639378633,1639378633);
insert  into `fc_tx_order`(`id`,`tx_hash`,`is_sync`,`deleted`,`create_time`,`update_time`) values (165,'0x0acb3ed73aa809d7fe08db9ede87cd8214885f19760be9a90c0c68271a2a8bb8',1,0,1639379743,1639379743);
insert  into `fc_tx_order`(`id`,`tx_hash`,`is_sync`,`deleted`,`create_time`,`update_time`) values (166,'0xeb7631164bc66fa5d304c25a68e8dd480f39262b341d665801cb81b48a54409a',1,0,1639379983,1639379983);
insert  into `fc_tx_order`(`id`,`tx_hash`,`is_sync`,`deleted`,`create_time`,`update_time`) values (167,'0x1fbf061a0ba1b1c505686ef4f993fa7db08a9ebd5d1266b2d20345fb1230b4ae',1,0,1639380283,1639380283);
insert  into `fc_tx_order`(`id`,`tx_hash`,`is_sync`,`deleted`,`create_time`,`update_time`) values (168,'0x691fe13e8f24cb1587d107c9caa39e0f1d284f6a59bd56cd1413c73312978d86',1,0,1639380553,1639380553);
insert  into `fc_tx_order`(`id`,`tx_hash`,`is_sync`,`deleted`,`create_time`,`update_time`) values (169,'0x8a0868177fcbad71bae832b4ca623d41805a007e361da0848b45a5545a09fe68',1,0,1639380649,1639380649);

/*Table structure for table `fc_user` */

DROP TABLE IF EXISTS `fc_user`;

CREATE TABLE `fc_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `nickname` varchar(100) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `address` varchar(50) NOT NULL DEFAULT '' COMMENT '钱包账号地址',
  `login_type` char(1) NOT NULL DEFAULT '1' COMMENT '登录类型',
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip` varchar(63) DEFAULT NULL COMMENT '最后登录ip',
  `user_verify` int(11) DEFAULT '0' COMMENT '用户是否验证',
  `short_url` varchar(512) DEFAULT NULL COMMENT '用户短地址',
  `brief` varchar(200) DEFAULT NULL COMMENT '用户简介',
  `banner_url` varchar(512) DEFAULT '',
  `is_web` tinyint(1) DEFAULT NULL COMMENT '是否属于本站用户',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index2` (`address`),
  KEY `index3` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_user` */

insert  into `fc_user`(`id`,`nickname`,`avatar`,`address`,`login_type`,`last_login_time`,`last_login_ip`,`user_verify`,`short_url`,`brief`,`banner_url`,`is_web`,`deleted`,`create_time`,`update_time`) values (58,NULL,NULL,'0x80dd336deb7eea1923780a7383c806870c9ce27d','1',1639380505,'127.0.0.1',0,NULL,NULL,'',1,0,1639376031,1639376031);
insert  into `fc_user`(`id`,`nickname`,`avatar`,`address`,`login_type`,`last_login_time`,`last_login_ip`,`user_verify`,`short_url`,`brief`,`banner_url`,`is_web`,`deleted`,`create_time`,`update_time`) values (59,NULL,NULL,'0xc2c7c3ef46a07806c50a34a0c661cae55afde899','1',1639379046,'192.168.1.8',0,NULL,NULL,'',1,0,1639378022,1639378022);
insert  into `fc_user`(`id`,`nickname`,`avatar`,`address`,`login_type`,`last_login_time`,`last_login_ip`,`user_verify`,`short_url`,`brief`,`banner_url`,`is_web`,`deleted`,`create_time`,`update_time`) values (60,NULL,NULL,'0x65957894d458ab763fdf6242986c3388b4b7404b','1',1639380588,'127.0.0.1',0,NULL,NULL,'',1,0,1639378841,1639378841);

/*Table structure for table `fc_user_follow` */

DROP TABLE IF EXISTS `fc_user_follow`;

CREATE TABLE `fc_user_follow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `user_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户地址',
  `following_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关注人地址',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index3` (`user_address`,`following_address`),
  KEY `index1` (`user_address`),
  KEY `index2` (`following_address`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `fc_user_follow` */

/*Table structure for table `fc_user_log` */

DROP TABLE IF EXISTS `fc_user_log`;

CREATE TABLE `fc_user_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户地址',
  `type` int(11) DEFAULT '0' COMMENT '登录类型1：登录，2: 退出',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户ip地址',
  `action` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作动作',
  `status` tinyint(1) DEFAULT NULL COMMENT '操作状态',
  `result` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作结果，或者成功消息，或者失败消息',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户登录日志';

/*Data for the table `fc_user_log` */

/*Table structure for table `fc_user_token` */

DROP TABLE IF EXISTS `fc_user_token`;

CREATE TABLE `fc_user_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_address` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户钱包地址',
  `user_token` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户登录token',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户登录token表';

/*Data for the table `fc_user_token` */

insert  into `fc_user_token`(`id`,`user_address`,`user_token`,`deleted`,`create_time`,`update_time`) values (44,'0x80dd336deb7eea1923780a7383c806870c9ce27d','eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0aGlzIGlzIGZpbmdlciBuZnQgdG9rZW4iLCJhdWQiOiJEQVBQQ0hBSU4iLCJhZGRyZXNzIjoiMHg4MGRkMzM2ZGViN2VlYTE5MjM3ODBhNzM4M2M4MDY4NzBjOWNlMjdkIiwiaXNzIjoiRmluZ2VybmZ0IiwiZXhwIjoxNjQyMDU4OTA1LCJpYXQiOjE2MzkzODA1MDV9.bHWpGwqKlx67XyEZEUT646OjXudiXWc25Q9HcjwiMYc',0,1639376031,1639380505);
insert  into `fc_user_token`(`id`,`user_address`,`user_token`,`deleted`,`create_time`,`update_time`) values (45,'0xc2c7c3ef46a07806c50a34a0c661cae55afde899','eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0aGlzIGlzIGZpbmdlciBuZnQgdG9rZW4iLCJhdWQiOiJEQVBQQ0hBSU4iLCJhZGRyZXNzIjoiMHhjMmM3YzNlZjQ2YTA3ODA2YzUwYTM0YTBjNjYxY2FlNTVhZmRlODk5IiwiaXNzIjoiRmluZ2VybmZ0IiwiZXhwIjoxNjQyMDU3NDQ2LCJpYXQiOjE2MzkzNzkwNDZ9.Kpu3IqrzH0wVFDEUQKU3P5nF_UBq3sPND0ZKWmG60lM',0,1639378022,1639379046);
insert  into `fc_user_token`(`id`,`user_address`,`user_token`,`deleted`,`create_time`,`update_time`) values (46,'0x65957894d458ab763fdf6242986c3388b4b7404b','eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0aGlzIGlzIGZpbmdlciBuZnQgdG9rZW4iLCJhdWQiOiJEQVBQQ0hBSU4iLCJhZGRyZXNzIjoiMHg2NTk1Nzg5NGQ0NThhYjc2M2ZkZjYyNDI5ODZjMzM4OGI0Yjc0MDRiIiwiaXNzIjoiRmluZ2VybmZ0IiwiZXhwIjoxNjQyMDU4OTg4LCJpYXQiOjE2MzkzODA1ODh9.6T9yq3BYPpwxoUCjm4rRx3nXVSWsQ91o977iG3V17AA',0,1639378841,1639380588);

/*Table structure for table `sta_buyer_seller` */

DROP TABLE IF EXISTS `sta_buyer_seller`;

CREATE TABLE `sta_buyer_seller` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_address` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户地址',
  `type` int(11) DEFAULT NULL COMMENT '用户类型（4:买，8:卖）',
  `tranfer_amount` int(11) DEFAULT NULL COMMENT '交易金额',
  `sta_time` bigint(20) DEFAULT '0' COMMENT '统计时间',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '是否逻辑删除',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='统计买家和卖家';

/*Data for the table `sta_buyer_seller` */

/*Table structure for table `sta_nft` */

DROP TABLE IF EXISTS `sta_nft`;

CREATE TABLE `sta_nft` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'nft地址',
  `count_nft` int(11) DEFAULT NULL COMMENT ' 总计nft',
  `reviewed_nft` int(11) DEFAULT NULL COMMENT '待审核nft',
  `newly_added_nft` int(11) DEFAULT NULL COMMENT '新增nft',
  `change_hands_nft` int(11) DEFAULT NULL COMMENT '转手nft',
  `sta_time` bigint(20) DEFAULT NULL COMMENT '统计时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `sta_nft` */

/*Table structure for table `sta_nft_deal` */

DROP TABLE IF EXISTS `sta_nft_deal`;

CREATE TABLE `sta_nft_deal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'nft地址',
  `total_flow` decimal(20,4) DEFAULT NULL COMMENT '总流水',
  `gas_money` decimal(20,4) DEFAULT NULL COMMENT 'gas费用',
  `platform_money` decimal(20,4) DEFAULT NULL COMMENT '平台佣金',
  `now_profit` decimal(20,4) DEFAULT NULL COMMENT '当前盈利',
  `sales_volume` int(11) DEFAULT NULL COMMENT '销售数量',
  `sum_money` decimal(20,4) DEFAULT NULL COMMENT '总计金额',
  `sta_time` bigint(20) DEFAULT '0' COMMENT '统计时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='nft销量统计';

/*Data for the table `sta_nft_deal` */

/*Table structure for table `sta_user` */

DROP TABLE IF EXISTS `sta_user`;

CREATE TABLE `sta_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_amount` int(11) DEFAULT '0' COMMENT '登录人数',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT '0' COMMENT '更新时间',
  `sta_time` bigint(20) DEFAULT '0' COMMENT '统计时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='统计登录人数表';

/*Data for the table `sta_user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
