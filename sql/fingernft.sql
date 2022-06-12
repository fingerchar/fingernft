-- MySQL dump 10.13  Distrib 8.0.24, for Linux (x86_64)
--
-- Host: localhost    Database: github
-- ------------------------------------------------------
-- Server version	8.0.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `fc_admin_department`
--

DROP TABLE IF EXISTS `fc_admin_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_admin_department` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `icon` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '图标地址',
  `desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '描述',
  `sort` int NOT NULL COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 0：停用，1：启用(默认为1)',
  `create_user` bigint NOT NULL,
  `update_user` bigint NOT NULL,
  `delete_user` bigint NOT NULL,
  `delete_time` bigint NOT NULL,
  `parent` bigint NOT NULL DEFAULT '0',
  `code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `job_id` bigint NOT NULL DEFAULT '0' COMMENT '职位ID',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_admin_department`
--

LOCK TABLES `fc_admin_department` WRITE;
/*!40000 ALTER TABLE `fc_admin_department` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_admin_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_admin_job`
--

DROP TABLE IF EXISTS `fc_admin_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_admin_job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '职位名称',
  `icon` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '图标地址',
  `desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '描述',
  `sort` int NOT NULL COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 0：停用，1：启用(默认为1)',
  `create_user` bigint NOT NULL,
  `update_user` bigint NOT NULL,
  `delete_user` bigint NOT NULL,
  `delete_time` bigint NOT NULL,
  `data_role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '数据权限id',
  `job_role` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '特定职位权限',
  `update_time` bigint NOT NULL,
  `create_time` bigint NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_admin_job`
--

LOCK TABLES `fc_admin_job` WRITE;
/*!40000 ALTER TABLE `fc_admin_job` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_admin_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_admin_log`
--

DROP TABLE IF EXISTS `fc_admin_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_admin_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理员',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理员地址',
  `type` int NOT NULL COMMENT '操作分类',
  `action` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作动作',
  `status` tinyint(1) NOT NULL COMMENT '操作状态',
  `result` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作结果，或者成功消息，或者失败消息',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '补充信息',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_admin_log`
--

LOCK TABLES `fc_admin_log` WRITE;
/*!40000 ALTER TABLE `fc_admin_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_admin_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_admin_notice`
--

DROP TABLE IF EXISTS `fc_admin_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_admin_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `notice_id` bigint NOT NULL COMMENT '通知ID',
  `notice_title` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '通知标题',
  `admin_id` bigint NOT NULL COMMENT '接收通知的管理员ID',
  `read_time` bigint NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_admin_notice`
--

LOCK TABLES `fc_admin_notice` WRITE;
/*!40000 ALTER TABLE `fc_admin_notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_admin_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_admin_permission`
--

DROP TABLE IF EXISTS `fc_admin_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_admin_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '权限',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=288 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_admin_permission`
--

LOCK TABLES `fc_admin_permission` WRITE;
/*!40000 ALTER TABLE `fc_admin_permission` DISABLE KEYS */;
INSERT INTO `fc_admin_permission` VALUES (1,1,'*',0,0,0);
/*!40000 ALTER TABLE `fc_admin_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_admin_role`
--

DROP TABLE IF EXISTS `fc_admin_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_admin_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `desc` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `is_system` tinyint(1) NOT NULL DEFAULT '0',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_admin_role`
--

LOCK TABLES `fc_admin_role` WRITE;
/*!40000 ALTER TABLE `fc_admin_role` DISABLE KEYS */;
INSERT INTO `fc_admin_role` VALUES (1,'超级管理员','所有模块的权限',1,1,0,1546300800,1546300800);
/*!40000 ALTER TABLE `fc_admin_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_admin_storage`
--

DROP TABLE IF EXISTS `fc_admin_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_admin_storage` (
  `id` bigint(20) unsigned zerofill NOT NULL,
  `key` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件的唯一索引',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名',
  `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件类型',
  `size` int NOT NULL COMMENT '文件大小',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文件访问链接',
  `ipfsHash` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_admin_storage`
--

LOCK TABLES `fc_admin_storage` WRITE;
/*!40000 ALTER TABLE `fc_admin_storage` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_admin_storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_admin_token`
--

DROP TABLE IF EXISTS `fc_admin_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_admin_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL DEFAULT '0',
  `key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `value` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `minutes` int NOT NULL DEFAULT '0',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '1 在线：0 下线',
  `shop_id` int NOT NULL DEFAULT '0' COMMENT '门店ID',
  `department_id` int NOT NULL DEFAULT '0' COMMENT '部门id',
  `org_id` int NOT NULL DEFAULT '0' COMMENT '队列ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` bigint NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_admin_token`
--

LOCK TABLES `fc_admin_token` WRITE;
/*!40000 ALTER TABLE `fc_admin_token` DISABLE KEYS */;
INSERT INTO `fc_admin_token` VALUES (1,1,'40e3f5e6-2193-4896-8139-5b73da86c3de',NULL,0,1,0,0,0,0,1649945084,1654912227);
/*!40000 ALTER TABLE `fc_admin_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_admin_user`
--

DROP TABLE IF EXISTS `fc_admin_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_admin_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '头像图片',
  `username` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理员名称',
  `nickname` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `password` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '管理员密码',
  `last_login_ip` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '最近一次登录IP地址',
  `last_login_time` bigint NOT NULL DEFAULT '0',
  `role_ids` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '[]' COMMENT '角色列表',
  `phone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `department_id` bigint NOT NULL DEFAULT '0' COMMENT '部门id',
  `job_id` bigint NOT NULL DEFAULT '0' COMMENT '对应job表中职位',
  `wechat` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '微信号',
  `wechat_qrcode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '微信二维码',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用：0否，1是',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint unsigned NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_admin_user`
--

LOCK TABLES `fc_admin_user` WRITE;
/*!40000 ALTER TABLE `fc_admin_user` DISABLE KEYS */;
INSERT INTO `fc_admin_user` VALUES (1,'','admin','admin','$2a$10$iwElLSuWI8F.iiLxqgNhIOMI0ZX0oBoifRQBF.31wZDkMWghgrPiG','192.168.1.7',1654912227,'[1]','',0,0,'','',0,0,0,1654912227);
/*!40000 ALTER TABLE `fc_admin_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_contract`
--

DROP TABLE IF EXISTS `fc_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_contract` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'nft name',
  `symbol` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'nft symbol',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '合约地址',
  `short_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '短地址',
  `version` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '合约版本',
  `cover` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '图标',
  `storage_id` bigint NOT NULL DEFAULT '0' COMMENT '图标保存Id',
  `owner` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '合约拥有者',
  `is_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是官方合约',
  `verify` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已验证',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '合约描述',
  `last_token_id` bigint NOT NULL DEFAULT '0' COMMENT '上一次增发的tokenId',
  `banner_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '封面',
  `get_info_times` int NOT NULL DEFAULT '0' COMMENT '获取name和symbol次数',
  `is_royalties` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否支持版权',
  `signer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '签名人账号',
  `is_sync` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已经同步',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index3` (`symbol`),
  KEY `owner` (`owner`),
  KEY `verify` (`verify`),
  KEY `is_admin` (`is_admin`),
  KEY `is_sync` (`is_sync`),
  KEY `deleted` (`deleted`),
  KEY `address` (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_contract`
--

LOCK TABLES `fc_contract` WRITE;
/*!40000 ALTER TABLE `fc_contract` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_contract_nft`
--

DROP TABLE IF EXISTS `fc_contract_nft`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_contract_nft` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `contract_id` bigint NOT NULL DEFAULT '0' COMMENT '合约id',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '合约地址',
  `name` varchar(200) NOT NULL DEFAULT '',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `category_id` bigint NOT NULL DEFAULT '0' COMMENT '分类id',
  `storage_id` bigint DEFAULT '0' COMMENT '图片保存Id',
  `token_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '对应tokenId',
  `royalties` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '版权',
  `nft_verify` int NOT NULL DEFAULT '0' COMMENT '是否已验证',
  `is_sync` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已同步链',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'nft拥有者',
  `tx_hash` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '区块链交易hash值',
  `metadata_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '资源地址',
  `metadata_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '资源内容',
  `get_meta_times` int DEFAULT '0' COMMENT '获取资源次数',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index2` (`address`),
  KEY `index3` (`creator`),
  KEY `index4` (`token_id`),
  KEY `index5` (`is_sync`),
  KEY `index6` (`create_time`),
  KEY `nft_verify` (`nft_verify`),
  KEY `deleted` (`deleted`),
  FULLTEXT KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_contract_nft`
--

LOCK TABLES `fc_contract_nft` WRITE;
/*!40000 ALTER TABLE `fc_contract_nft` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_contract_nft` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_nft_category`
--

DROP TABLE IF EXISTS `fc_nft_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_nft_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `name` varchar(100) NOT NULL COMMENT '类目名称',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  `order` int NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `order` (`order`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_nft_category`
--

LOCK TABLES `fc_nft_category` WRITE;
/*!40000 ALTER TABLE `fc_nft_category` DISABLE KEYS */;
INSERT INTO `fc_nft_category` VALUES (1,'Animation',0,1649042088,1649042088,1),(2,'Illustration',0,1649042130,1649042130,2),(3,'gamefi',0,1649042141,1649042141,3);
/*!40000 ALTER TABLE `fc_nft_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_nft_items`
--

DROP TABLE IF EXISTS `fc_nft_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_nft_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `token_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `price` varchar(50) NOT NULL DEFAULT '' COMMENT '价格',
  `usdt_price` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'usdt价格',
  `paytoken_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '支付币种地址',
  `paytoken_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '支付币种名称',
  `paytoken_decimals` int NOT NULL DEFAULT '0' COMMENT '支付币种精确度',
  `paytoken_symbol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '支付币种符号',
  `signature` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '签名',
  `item_owner` varchar(50) NOT NULL COMMENT 'token拥有者',
  `category_id` bigint NOT NULL DEFAULT '0',
  `onsell` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否在售',
  `onsell_time` bigint NOT NULL DEFAULT '0' COMMENT '发布售卖的时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  `is_sync` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index2` (`item_owner`),
  KEY `index3` (`address`),
  KEY `index6` (`onsell`),
  KEY `token_id` (`token_id`),
  KEY `deleted` (`deleted`),
  KEY `is_sync` (`is_sync`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_nft_items`
--

LOCK TABLES `fc_nft_items` WRITE;
/*!40000 ALTER TABLE `fc_nft_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_nft_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_nft_like`
--

DROP TABLE IF EXISTS `fc_nft_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_nft_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `nft_id` bigint NOT NULL DEFAULT '0' COMMENT 'nft id',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `token_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `user_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户地址',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index4` (`user_address`),
  KEY `address` (`address`),
  KEY `token_id` (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_nft_like`
--

LOCK TABLES `fc_nft_like` WRITE;
/*!40000 ALTER TABLE `fc_nft_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_nft_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_notice`
--

DROP TABLE IF EXISTS `fc_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `type` tinyint NOT NULL DEFAULT '0' COMMENT '大类型1follow 2like 3trade',
  `sub_type` int NOT NULL DEFAULT '0' COMMENT '消息类型：0: "LIKE"\\n1: "FOLLOWING"\\n2: "ORDER"\\n3: "BID"\\n4: "BUY"\\n5: "SALE"\\n6: "CANCEL"\\n7: "CANCEL_BID"\\n8: "TRANSFER"\\n9: "MINT"\\n10: "BURN"',
  `owner` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '消息对应用户地址',
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作者地址',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读',
  `image` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '显示图片',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '显示名称',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index2` (`owner`),
  KEY `deleted` (`deleted`),
  KEY `is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_notice`
--

LOCK TABLES `fc_notice` WRITE;
/*!40000 ALTER TABLE `fc_notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_order`
--

DROP TABLE IF EXISTS `fc_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
  `order_type` int DEFAULT NULL COMMENT '订单类型1:SALE  2:BID',
  `usdt_price` varchar(64) DEFAULT NULL COMMENT 'usdt价格',
  `sells` varchar(256) DEFAULT '0' COMMENT '已经售卖数量',
  `paytoken_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `paytoken_name` varchar(50) NOT NULL DEFAULT '',
  `paytoken_decimals` int DEFAULT NULL,
  `paytoken_symbol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '',
  `buy_fee` int DEFAULT NULL,
  `sell_fee` int DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_order`
--

LOCK TABLES `fc_order` WRITE;
/*!40000 ALTER TABLE `fc_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_order_log`
--

DROP TABLE IF EXISTS `fc_order_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_order_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `order_id` bigint NOT NULL DEFAULT '0' COMMENT '订单id',
  `from` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '订单发起人地址',
  `to` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '交易对象地址',
  `type` int NOT NULL DEFAULT '0' COMMENT '交易类型sale_cancel, sale_buy, bid_cancel, bid_accept, erc721_create, erc1155_create, token_tranfer, token_burn',
  `tx_hash` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '交易hash',
  `pre_log_id` bigint NOT NULL DEFAULT '0' COMMENT '前一个订单日志id',
  `token` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作NFT的地址',
  `token_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作NFT的tokenId',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志体',
  `expired` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否过期',
  `paytoken_address` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `paytoken_name` varchar(60) NOT NULL DEFAULT '',
  `paytoken_symbol` varchar(60) NOT NULL DEFAULT '',
  `paytoken_decimals` int NOT NULL DEFAULT '0',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index2` (`order_id`),
  KEY `index3` (`type`),
  KEY `index4` (`token`),
  KEY `index5` (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_order_log`
--

LOCK TABLES `fc_order_log` WRITE;
/*!40000 ALTER TABLE `fc_order_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_order_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_pay_token`
--

DROP TABLE IF EXISTS `fc_pay_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_pay_token` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `name` varchar(100) NOT NULL COMMENT 'token名称',
  `symbol` varchar(100) NOT NULL COMMENT 'token symbol',
  `decimals` int NOT NULL COMMENT 'token 精度',
  `rate` decimal(24,16) NOT NULL DEFAULT '0.0000000000000000' COMMENT 'token 汇率',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'token 合约地址',
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'token 图像',
  `storage_id` bigint NOT NULL DEFAULT '0' COMMENT 'token 图像保存id',
  `type` int NOT NULL DEFAULT '0' COMMENT '0 ETH 1 WETH 2 ERC1155 3 ERC721 4 ERC721Deprecated',
  `is_default` tinyint NOT NULL DEFAULT '0' COMMENT '默认支付类型',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `name` (`name`),
  KEY `symbol` (`symbol`),
  KEY `address` (`address`),
  KEY `deleted` (`deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_pay_token`
--

LOCK TABLES `fc_pay_token` WRITE;
/*!40000 ALTER TABLE `fc_pay_token` DISABLE KEYS */;
INSERT INTO `fc_pay_token` VALUES (1,'ETH','ETH',18,0.0000000000000000,'0x0000000000000000000000000000000000000000','/static/upload/6z9zvp2adqic1q2lmw8y.png',0,0,0,0,1648990142,1648990142);
/*!40000 ALTER TABLE `fc_pay_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_storage`
--

DROP TABLE IF EXISTS `fc_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件存储表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_storage`
--

LOCK TABLES `fc_storage` WRITE;
/*!40000 ALTER TABLE `fc_storage` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_system`
--

DROP TABLE IF EXISTS `fc_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_system` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `key_name` varchar(255) NOT NULL COMMENT '系统配置名',
  `key_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统配置值',
  `show` tinyint(1) DEFAULT '1',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index2` (`key_name`),
  UNIQUE KEY `key_name` (`key_name`)
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_system`
--

LOCK TABLES `fc_system` WRITE;
/*!40000 ALTER TABLE `fc_system` DISABLE KEYS */;
INSERT INTO `fc_system` VALUES (1,'ipfsServerIp','192.168.1.111',0,1625305504,1648171957,0),(2,'ipfsServerPort','5001',0,1625305504,1639378180,0),(3,'ipfsRemoteServer','',0,1625305504,1639367121,0),(6,'staticLocalPath','/data/java',0,1625305504,1647331406,0),(7,'storageRequestBase','/static/upload/',0,1625305504,1625305504,0),(14,'ipfsUrl','http://192.168.1.111:8080',1,1619549758,1648208732,0),(15,'sellerFee','250',1,1619549758,1619549758,0),(16,'buyerFee','250',1,1619549758,1644232047,0),(23,'cdnUrl','https://fingernft.fingerchar.com',1,1619549758,1647331909,0),(24,'lastBlock','100',0,1619549758,1647930911,0),(25,'nftDefaultVerify','true',0,1619549758,1643097825,0),(26,'loginMessage','Welcome. Login Finger NFT Demo Market. This is completely secure and doesn\'t cost anything!',1,1619549758,1645611557,0),(27,'website','https://fingernft.fingerchar.com',1,1619549758,1647331905,0),(119,'blockConfirmation','3',0,NULL,1648626344,0),(120,'maxBlockOneTime','10',0,NULL,NULL,0),(124,'configNetwork','',1,NULL,1648261781,0),(127,'configStake','',1,NULL,1648430679,0),(128,'configDeploy','',1,NULL,1648264596,0),(130,'configContract','',1,NULL,1648262962,0),(131,'gasTracker','',1,1649945462,1649945462,0);
/*!40000 ALTER TABLE `fc_system` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_tx_order`
--

DROP TABLE IF EXISTS `fc_tx_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_tx_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `tx_hash` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '交易hash',
  `block_number` int NOT NULL DEFAULT '0',
  `block_timestamp` int NOT NULL DEFAULT '0',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint DEFAULT NULL,
  `update_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tx_hash_2` (`tx_hash`),
  KEY `block_number` (`block_number`),
  KEY `tx_hash` (`tx_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_tx_order`
--

LOCK TABLES `fc_tx_order` WRITE;
/*!40000 ALTER TABLE `fc_tx_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_tx_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_user`
--

DROP TABLE IF EXISTS `fc_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '头像',
  `address` varchar(50) NOT NULL DEFAULT '' COMMENT '钱包账号地址',
  `login_type` char(1) NOT NULL DEFAULT '1' COMMENT '登录类型',
  `last_login_time` bigint NOT NULL DEFAULT '0',
  `last_login_ip` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '最后登录ip',
  `user_verify` int NOT NULL DEFAULT '0' COMMENT '用户是否验证',
  `short_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户短地址',
  `brief` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户简介',
  `banner_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `is_web` tinyint(1) NOT NULL COMMENT '是否属于本站用户',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index2` (`address`),
  KEY `index3` (`nickname`),
  KEY `deleted` (`deleted`),
  KEY `user_verify` (`user_verify`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_user`
--

LOCK TABLES `fc_user` WRITE;
/*!40000 ALTER TABLE `fc_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_user_follow`
--

DROP TABLE IF EXISTS `fc_user_follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_user_follow` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `user_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户地址',
  `following_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '关注人地址',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0',
  `update_time` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_address` (`user_address`),
  KEY `following_address` (`following_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_user_follow`
--

LOCK TABLES `fc_user_follow` WRITE;
/*!40000 ALTER TABLE `fc_user_follow` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_user_follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_user_log`
--

DROP TABLE IF EXISTS `fc_user_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_user_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户地址',
  `type` int NOT NULL DEFAULT '0' COMMENT '登录类型1：登录，2: 退出',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户ip地址',
  `action` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作动作',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '操作状态',
  `result` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作结果，或者成功消息，或者失败消息',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `address` (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户登录日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_user_log`
--

LOCK TABLES `fc_user_log` WRITE;
/*!40000 ALTER TABLE `fc_user_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_user_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fc_user_token`
--

DROP TABLE IF EXISTS `fc_user_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fc_user_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_address` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户钱包地址',
  `user_token` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户登录token',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` bigint NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_address` (`user_address`),
  KEY `deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户登录token表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fc_user_token`
--

LOCK TABLES `fc_user_token` WRITE;
/*!40000 ALTER TABLE `fc_user_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `fc_user_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'github'
--

--
-- Dumping routines for database 'github'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-10 21:59:22
