/*
 Navicat Premium Data Transfer

 Source Server         : local-docker
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:33060
 Source Schema         : shop

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 27/06/2021 00:02:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pms_product_categories
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_categories`;
CREATE TABLE `pms_product_categories`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '上机分类的编号：0表示一级分类',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `level` int(1) NULL DEFAULT NULL COMMENT '分类级别：0->1级；1->2级',
  `sub_count` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '子分类数量',
  `product_count` int(11) NULL DEFAULT NULL,
  `product_unit` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nav_status` int(1) NULL DEFAULT NULL COMMENT '是否显示在导航栏：0->不显示；1->显示',
  `show_status` int(1) NULL DEFAULT NULL COMMENT '显示状态：0->不显示；1->显示',
  `sort` int(11) NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `keywords` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_categories
-- ----------------------------
INSERT INTO `pms_product_categories` VALUES (1, 0, '服装', 0, 1, 100, '件', 1, 1, 1, NULL, '服装', '服装分类', NULL, '2021-06-26 14:04:51');
INSERT INTO `pms_product_categories` VALUES (2, 0, '手机数码', 0, 1, 100, '件', 1, 0, 1, NULL, '手机数码', '手机数码', NULL, '2021-06-26 14:07:25');
INSERT INTO `pms_product_categories` VALUES (3, 0, '家用电器', 0, 1, 100, '件', 1, 1, 1, NULL, '家用电器', '家用电器', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (4, 0, '家具家装', 0, 1, 100, '件', 1, 1, 1, NULL, '家具家装', '家具家装', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (5, 0, '汽车用品', 0, 1, 100, '件', 1, 0, 1, NULL, '汽车用品', '汽车用品', NULL, '2021-06-26 11:14:13');
INSERT INTO `pms_product_categories` VALUES (7, 1, '外套', 1, 1, 100, '件', 1, 1, 0, '', '外套', '外套', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (8, 1, 'T恤', 1, 1, 100, '件', 1, 1, 0, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'T恤', 'T恤', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (9, 1, '休闲裤', 1, 1, 100, '件', 1, 1, 0, NULL, '休闲裤', '休闲裤', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (10, 1, '牛仔裤', 1, 1, 100, '件', 1, 1, 0, NULL, '牛仔裤', '牛仔裤', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (11, 1, '衬衫', 1, 1, 100, '件', 1, 1, 0, NULL, '衬衫', '衬衫分类', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (13, 12, '家电子分类1', 1, 1, 1, 'string', 0, 1, 0, 'string', 'string', 'string', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (14, 12, '家电子分类2', 1, 1, 1, 'string', 0, 1, 0, 'string', 'string', 'string', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (19, 2, '手机通讯', 1, 0, 0, '件', 0, 0, 0, '', '手机通讯', '手机通讯', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (29, 1, '男鞋', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (30, 2, '手机配件', 1, 0, 0, '', 0, 0, 0, '', '手机配件', '手机配件', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (31, 2, '摄影摄像', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (32, 2, '影音娱乐', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (33, 2, '数码配件', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (34, 2, '智能设备', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (35, 3, '电视', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (36, 3, '空调', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (37, 3, '洗衣机', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (38, 3, '冰箱', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (39, 3, '厨卫大电', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (40, 3, '厨房小电', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (41, 3, '生活电器', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (42, 3, '个护健康', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (43, 4, '厨房卫浴', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (44, 4, '灯饰照明', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (45, 4, '五金工具', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (46, 4, '卧室家具', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (47, 4, '客厅家具', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (48, 5, '全新整车', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (49, 5, '车载电器', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (50, 5, '维修保养', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (51, 5, '汽车装饰', 1, 0, 0, '', 0, 0, 0, '', '', '', NULL, NULL);
INSERT INTO `pms_product_categories` VALUES (54, 0, '分类3', 1, 0, NULL, NULL, NULL, 1, 999, NULL, '分类3关键字', NULL, '2021-06-26 22:58:44', '2021-06-26 23:02:43');

-- ----------------------------
-- Table structure for pms_products
-- ----------------------------
DROP TABLE IF EXISTS `pms_products`;
CREATE TABLE `pms_products`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand_id` bigint(20) NULL DEFAULT NULL,
  `category_id` bigint(20) NULL DEFAULT NULL,
  `feight_template_id` bigint(20) NULL DEFAULT NULL,
  `product_attribute_category_id` bigint(20) NULL DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `product_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '货号',
  `delete_status` int(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `publish_status` int(1) NULL DEFAULT NULL COMMENT '上架状态：0->下架；1->上架',
  `new_status` int(1) NULL DEFAULT NULL COMMENT '新品状态:0->不是新品；1->新品',
  `recommand_status` int(1) NULL DEFAULT NULL COMMENT '推荐状态；0->不推荐；1->推荐',
  `verify_status` int(1) NULL DEFAULT NULL COMMENT '审核状态：0->未审核；1->审核通过',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `sale` int(11) NULL DEFAULT NULL COMMENT '销量',
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `promotion_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '促销价格',
  `gift_growth` int(11) NULL DEFAULT 0 COMMENT '赠送的成长值',
  `gift_point` int(11) NULL DEFAULT 0 COMMENT '赠送的积分',
  `use_point_limit` int(11) NULL DEFAULT NULL COMMENT '限制使用的积分数',
  `sub_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品描述',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '市场价',
  `stock` int(11) NULL DEFAULT NULL COMMENT '库存',
  `low_stock` int(11) NULL DEFAULT NULL COMMENT '库存预警值',
  `unit` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品重量，默认为克',
  `preview_status` int(1) NULL DEFAULT NULL COMMENT '是否为预告商品：0->不是；1->是',
  `service_ids` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮',
  `keywords` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `album_pics` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '画册图片，连产品图片限制为5张，以逗号分割',
  `detail_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `detail_desc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `detail_html` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '产品详情网页内容',
  `detail_mobile_html` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '移动端网页详情',
  `promotion_start_time` datetime(0) NULL DEFAULT NULL COMMENT '促销开始时间',
  `promotion_end_time` datetime(0) NULL DEFAULT NULL COMMENT '促销结束时间',
  `promotion_per_limit` int(11) NULL DEFAULT NULL COMMENT '活动限购数量',
  `promotion_type` int(1) NULL DEFAULT NULL COMMENT '促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购',
  `brand_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `product_category_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品分类名称',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_products
-- ----------------------------
INSERT INTO `pms_products` VALUES (1, 49, 7, 0, 0, '银色星芒刺绣网纱底裤', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86577', 1, 1, 1, 1, 1, 100, 0, 100.01, NULL, 0, 100, NULL, '111', '111', 120.00, 100, 20, '件', 1000.00, 0, NULL, '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', NULL, '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', NULL, NULL, NULL, 0, '七匹狼', '外套', '2021-06-25 20:35:38', NULL);
INSERT INTO `pms_products` VALUES (2, 49, 7, 0, 0, '银色星芒刺绣网纱底裤2', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86578', 1, 1, 1, 1, 1, 1, 0, 100.00, NULL, 0, 100, NULL, '111', '111', 120.00, 100, 20, '件', 1000.00, 0, NULL, '银色星芒刺绣网纱底裤2', '银色星芒刺绣网纱底裤', NULL, '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '<p>银色星芒刺绣网纱底裤</p>', '<p>银色星芒刺绣网纱底裤</p>', NULL, NULL, NULL, 0, '七匹狼', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (3, 1, 7, 0, 0, '银色星芒刺绣网纱底裤3', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86579', 1, 1, 1, 1, 1, 1, 0, 100.00, NULL, 0, 100, NULL, '111', '111', 120.00, 100, 20, '件', 1000.00, 0, NULL, '银色星芒刺绣网纱底裤3', '银色星芒刺绣网纱底裤', NULL, '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', NULL, NULL, NULL, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (4, 1, 7, 0, 0, '银色星芒刺绣网纱底裤4', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86580', 1, 1, 1, 1, 1, 1, 0, 100.00, NULL, 0, 100, NULL, '111', '111', 120.00, 100, 20, '件', 1000.00, 0, NULL, '银色星芒刺绣网纱底裤4', '银色星芒刺绣网纱底裤', NULL, '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', NULL, NULL, NULL, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (5, 1, 7, 0, 0, '银色星芒刺绣网纱底裤5', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86581', 1, 0, 1, 1, 1, 1, 0, 100.00, NULL, 0, 100, NULL, '111', '111', 120.00, 100, 20, '件', 1000.00, 0, NULL, '银色星芒刺绣网纱底裤5', '银色星芒刺绣网纱底裤', NULL, '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', NULL, NULL, NULL, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (6, 1, 7, 0, 0, '银色星芒刺绣网纱底裤6', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86582', 1, 1, 1, 1, 1, 1, 0, 100.00, NULL, 0, 100, NULL, '111', '111', 120.00, 100, 20, '件', 1000.00, 0, NULL, '银色星芒刺绣网纱底裤6', '银色星芒刺绣网纱底裤', NULL, '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', '银色星芒刺绣网纱底裤', NULL, NULL, NULL, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (7, 1, 7, 0, 1, '女式超柔软拉毛运动开衫', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86577', 1, 0, 0, 0, 0, 0, 0, 249.00, 0.00, 0, 100, 0, '匠心剪裁，垂感质地', '匠心剪裁，垂感质地', 299.00, 100, 0, '件', 0.00, 0, 'string', '女式超柔软拉毛运动开衫', 'string', 'string', 'string', 'string', 'string', 'string', '2018-04-26 10:41:03', '2018-04-26 10:41:03', 0, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (8, 1, 7, 0, 1, '女式超柔软拉毛运动开衫1', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86577', 1, 0, 0, 0, 0, 0, 0, 249.00, 0.00, 0, 100, 0, '匠心剪裁，垂感质地', '匠心剪裁，垂感质地', 299.00, 100, 0, '件', 0.00, 0, 'string', '女式超柔软拉毛运动开衫', 'string', 'string', 'string', 'string', 'string', 'string', '2018-04-26 10:41:03', '2018-04-26 10:41:03', 0, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (9, 1, 7, 0, 1, '女式超柔软拉毛运动开衫1', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86577', 1, 0, 0, 0, 0, 0, 0, 249.00, 0.00, 0, 100, 0, '匠心剪裁，垂感质地', '匠心剪裁，垂感质地', 299.00, 100, 0, '件', 0.00, 0, 'string', '女式超柔软拉毛运动开衫', 'string', 'string', 'string', 'string', 'string', 'string', '2018-04-26 10:41:03', '2018-04-26 10:41:03', 0, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (10, 1, 7, 0, 1, '女式超柔软拉毛运动开衫1', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86577', 1, 0, 0, 0, 0, 0, 0, 249.00, 0.00, 0, 100, 0, '匠心剪裁，垂感质地', '匠心剪裁，垂感质地', 299.00, 100, 0, '件', 0.00, 0, 'string', '女式超柔软拉毛运动开衫', 'string', 'string', 'string', 'string', 'string', 'string', '2018-04-26 10:41:03', '2018-04-26 10:41:03', 0, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (11, 1, 7, 0, 1, '女式超柔软拉毛运动开衫1', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86577', 1, 1, 0, 1, 0, 0, 0, 249.00, 0.00, 0, 100, 0, '匠心剪裁，垂感质地', '匠心剪裁，垂感质地', 299.00, 100, 0, '件', 0.00, 0, 'string', '女式超柔软拉毛运动开衫', 'string', 'string', 'string', 'string', 'string', 'string', '2018-04-26 10:41:03', '2018-04-26 10:41:03', 0, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (12, 1, 7, 0, 1, '女式超柔软拉毛运动开衫2', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86577', 1, 1, 0, 1, 0, 0, 0, 249.00, 0.00, 0, 100, 0, '匠心剪裁，垂感质地', '匠心剪裁，垂感质地', 299.00, 100, 0, '件', 0.00, 0, 'string', '女式超柔软拉毛运动开衫', 'string', 'string', 'string', 'string', 'string', 'string', '2018-04-26 10:41:03', '2018-04-26 10:41:03', 0, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (13, 1, 7, 0, 1, '女式超柔软拉毛运动开衫3', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86577', 1, 1, 0, 1, 0, 0, 0, 249.00, 0.00, 0, 100, 0, '匠心剪裁，垂感质地', '匠心剪裁，垂感质地', 299.00, 100, 0, '件', 0.00, 0, 'string', '女式超柔软拉毛运动开衫', 'string', 'string', 'string', 'string', 'string', 'string', '2018-04-26 10:41:03', '2018-04-26 10:41:03', 0, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (14, 1, 7, 0, 1, '女式超柔软拉毛运动开衫3', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86577', 1, 0, 0, 1, 0, 0, 0, 249.00, 0.00, 0, 100, 0, '匠心剪裁，垂感质地', '匠心剪裁，垂感质地', 299.00, 100, 0, '件', 0.00, 0, 'string', '女式超柔软拉毛运动开衫', 'string', 'string', 'string', 'string', 'string', 'string', '2018-04-26 10:41:03', '2018-04-26 10:41:03', 0, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (18, 1, 7, 0, 1, '女式超柔软拉毛运动开衫3', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180522/web.png', 'No86577', 1, 0, 0, 1, 0, 0, 0, 249.00, 0.00, 0, 100, 0, '匠心剪裁，垂感质地', '匠心剪裁，垂感质地', 299.00, 100, 0, '件', 0.00, 0, 'string', '女式超柔软拉毛运动开衫', 'string', 'string', 'string', 'string', 'string', 'string', '2018-04-26 10:41:03', '2018-04-26 10:41:03', 0, 0, '万和', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (22, 6, 7, 0, 1, 'test', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180604/1522738681.jpg', '', 1, 1, 0, 0, 0, 0, 0, 0.00, NULL, 0, 0, 0, 'test', '', 0.00, 100, 0, '', 0.00, 1, '1,2', '', '', '', '', '', '', '', NULL, NULL, 0, 0, '小米', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (23, 6, 19, 0, 1, '毛衫测试', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180604/1522738681.jpg', 'NO.1098', 1, 1, 1, 1, 0, 0, 0, 99.00, NULL, 99, 99, 1000, '毛衫测试11', 'xxx', 109.00, 100, 0, '件', 1000.00, 1, '1,2,3', '毛衫测试', '毛衫测试', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180604/1522738681.jpg,http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180604/1522738681.jpg', '毛衫测试', '毛衫测试', '<p><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180604/155x54.bmp\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180604/APP登录bg1080.jpg\" width=\"500\" height=\"500\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180604/APP登录界面.jpg\" width=\"500\" height=\"500\" /></p>', '', NULL, NULL, 0, 2, '小米', '手机数码', NULL, NULL);
INSERT INTO `pms_products` VALUES (24, 6, 7, 0, NULL, 'xxx', '', '', 1, 0, 0, 0, 0, 0, 0, 0.00, NULL, 0, 0, 0, 'xxx', '', 0.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '', '', NULL, NULL, 0, 0, '小米', '外套', NULL, NULL);
INSERT INTO `pms_products` VALUES (26, 3, 19, 0, 3, '华为 HUAWEI P20 ', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ac1bf58Ndefaac16.jpg', '6946605', 0, 1, 1, 1, 0, 100, 0, 3788.00, NULL, 3788, 3788, 0, 'AI智慧全面屏 6GB +64GB 亮黑色 全网通版 移动联通电信4G手机 双卡双待手机 双卡双待', '', 4288.00, 1000, 0, '件', 0.00, 1, '2,3,1', '', '', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ab46a3cN616bdc41.jpg,http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ac1bf5fN2522b9dc.jpg', '', '', '<p><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ad44f1cNf51f3bb0.jpg\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ad44fa8Nfcf71c10.jpg\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ad44fa9N40e78ee0.jpg\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ad457f4N1c94bdda.jpg\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ad457f5Nd30de41d.jpg\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5b10fb0eN0eb053fb.jpg\" /></p>', '', NULL, NULL, 0, 1, '华为', '手机通讯', NULL, NULL);
INSERT INTO `pms_products` VALUES (27, 6, 19, 0, 3, '小米8 全面屏游戏智能手机 6GB+64GB 黑色 全网通4G 双卡双待', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/xiaomi.jpg', '7437788', 0, 1, 1, 1, 0, 0, 0, 2699.00, NULL, 2699, 2699, 0, '骁龙845处理器，红外人脸解锁，AI变焦双摄，AI语音助手小米6X低至1299，点击抢购', '小米8 全面屏游戏智能手机 6GB+64GB 黑色 全网通4G 双卡双待', 2699.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '<p><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b2254e8N414e6d3a.jpg\" width=\"500\" /></p>', '', NULL, NULL, 0, 3, '小米', '手机数码', NULL, NULL);
INSERT INTO `pms_products` VALUES (28, 6, 19, 0, 3, '小米 红米5A 全网通版 3GB+32GB 香槟金 移动联通电信4G手机 双卡双待', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5a9d248cN071f4959.jpg', '7437789', 0, 1, 1, 1, 0, 0, 0, 649.00, NULL, 649, 649, 0, '8天超长待机，137g轻巧机身，高通骁龙处理器小米6X低至1299，点击抢购', '', 649.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '', '', NULL, NULL, 0, 4, '小米', '手机数码', NULL, NULL);
INSERT INTO `pms_products` VALUES (29, 51, 19, 0, 3, 'Apple iPhone 8 Plus 64GB 红色特别版 移动联通电信4G手机', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5acc5248N6a5f81cd.jpg', '7437799', 0, 1, 1, 1, 0, 0, 0, 5499.00, NULL, 5499, 5499, 0, '【限时限量抢购】Apple产品年中狂欢节，好物尽享，美在智慧！速来 >> 勾选[保障服务][原厂保2年]，获得AppleCare+全方位服务计划，原厂延保售后无忧。', '', 5499.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '', '', NULL, NULL, 0, 0, '苹果', '手机数码', NULL, NULL);
INSERT INTO `pms_products` VALUES (30, 50, 8, 0, 1, 'HLA海澜之家简约动物印花短袖T恤', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5ad83a4fN6ff67ecd.jpg!cc_350x449.jpg', 'HNTBJ2E042A', 0, 1, 1, 1, 0, 0, 0, 98.00, NULL, 0, 0, 0, '2018夏季新品微弹舒适新款短T男生 6月6日-6月20日，满300减30，参与互动赢百元礼券，立即分享赢大奖', '', 98.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '', '', NULL, NULL, 0, 0, '海澜之家', 'T恤', NULL, NULL);
INSERT INTO `pms_products` VALUES (31, 50, 8, 0, 1, 'HLA海澜之家蓝灰花纹圆领针织布短袖T恤', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5ac98b64N70acd82f.jpg!cc_350x449.jpg', 'HNTBJ2E080A', 0, 1, 0, 0, 0, 0, 0, 98.00, NULL, 0, 0, 0, '2018夏季新品短袖T恤男HNTBJ2E080A 蓝灰花纹80 175/92A/L80A 蓝灰花纹80 175/92A/L', '', 98.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '', '', NULL, NULL, 0, 0, '海澜之家', 'T恤', NULL, NULL);
INSERT INTO `pms_products` VALUES (32, 50, 8, 0, NULL, 'HLA海澜之家短袖T恤男基础款', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5a51eb88Na4797877.jpg', 'HNTBJ2E153A', 0, 1, 0, 0, 0, 0, 0, 68.00, NULL, 0, 0, 0, 'HLA海澜之家短袖T恤男基础款简约圆领HNTBJ2E153A藏青(F3)175/92A(50)', '', 68.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '', '', NULL, NULL, 0, 0, '海澜之家', 'T恤', NULL, NULL);
INSERT INTO `pms_products` VALUES (33, 6, 35, 0, NULL, '小米（MI）小米电视4A ', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b02804dN66004d73.jpg', '4609652', 0, 1, 0, 0, 0, 0, 0, 2499.00, NULL, 0, 0, 0, '小米（MI）小米电视4A 55英寸 L55M5-AZ/L55M5-AD 2GB+8GB HDR 4K超高清 人工智能网络液晶平板电视', '', 2499.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '', '', NULL, NULL, 0, 0, '小米', '手机数码', NULL, NULL);
INSERT INTO `pms_products` VALUES (34, 6, 35, 0, NULL, '小米（MI）小米电视4A 65英寸', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b028530N51eee7d4.jpg', '4609660', 0, 1, 0, 0, 0, 0, 0, 3999.00, NULL, 0, 0, 0, ' L65M5-AZ/L65M5-AD 2GB+8GB HDR 4K超高清 人工智能网络液晶平板电视', '', 3999.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '', '', NULL, NULL, 0, 0, '小米', '手机数码', NULL, NULL);
INSERT INTO `pms_products` VALUES (35, 58, 29, 0, 11, '耐克NIKE 男子 休闲鞋 ROSHE RUN 运动鞋 511881-010黑色41码', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b235bb9Nf606460b.jpg', '6799342', 0, 1, 0, 0, 0, 0, 0, 369.00, NULL, 0, 0, 0, '耐克NIKE 男子 休闲鞋 ROSHE RUN 运动鞋 511881-010黑色41码', '', 369.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '', '', NULL, NULL, 0, 0, 'NIKE', '男鞋', NULL, NULL);
INSERT INTO `pms_products` VALUES (36, 58, 29, 0, 11, '耐克NIKE 男子 气垫 休闲鞋 AIR MAX 90 ESSENTIAL 运动鞋 AJ1285-101白色41码', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b19403eN9f0b3cb8.jpg', '6799345', 0, 1, 1, 1, 0, 0, 0, 499.00, NULL, 0, 0, 0, '耐克NIKE 男子 气垫 休闲鞋 AIR MAX 90 ESSENTIAL 运动鞋 AJ1285-101白色41码', '', 499.00, 100, 0, '', 0.00, 0, '', '', '', '', '', '', '', '', NULL, NULL, 0, 0, 'NIKE', '男鞋', NULL, NULL);

-- ----------------------------
-- Table structure for ums_admins
-- ----------------------------
DROP TABLE IF EXISTS `ums_admins`;
CREATE TABLE `ums_admins`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门名称',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` enum('F','M') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'M' COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `avatar_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像真实路径',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `is_admin` bit(1) NULL DEFAULT b'0' COMMENT '是否为admin账号',
  `enabled` bit(1) NULL DEFAULT NULL COMMENT '状态：1启用、0禁用',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新着',
  `pwd_reset_time` datetime(0) NULL DEFAULT NULL COMMENT '修改密码的时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `uniq_username`(`username`) USING BTREE,
  UNIQUE INDEX `uniq_email`(`email`) USING BTREE,
  INDEX `FK5rwmryny6jthaaxkogownknqp`(`dept_id`) USING BTREE,
  INDEX `FKpq2dhypk2qgt68nauh2by22jb`(`avatar_name`) USING BTREE,
  INDEX `inx_enabled`(`enabled`) USING BTREE,
  INDEX `UK_kpubos9gc2cvtkb0thktkbkes`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_admins
-- ----------------------------
INSERT INTO `ums_admins` VALUES (1, 2, 'admin', '管理员', 'F', '18888888888', '201507802@qq.com', 'avatar-20200806032259161.png', '/Users/jie/Documents/work/me/admin/eladmin/~/avatar/avatar-20200806032259161.png', '$2a$10$IHvbumxtQoVhCjV6l6ERdubosGoFQIeYu5p458ESP3uLCuR.Z05Ku', b'1', b'1', NULL, 'admin', '2020-05-03 16:38:31', '2018-08-23 09:11:56', '2020-09-05 10:43:31');
INSERT INTO `ums_admins` VALUES (2, 17, 'test', '测试', 'M', '15199999999', '231@qq.com', NULL, NULL, '$2a$10$4XcyudOYTSz6fue6KFNMHeUQnCX5jbBQypLEnGk1PmekXt5c95JcK', b'0', b'0', 'admin', 'admin', NULL, '2020-05-05 11:15:49', '2021-06-16 14:30:42');
INSERT INTO `ums_admins` VALUES (3, 17, 'user01', 'user01', 'F', '15211111111', 'user01@test.com', NULL, NULL, '$2a$10$eYcLbbujbRyIXPpweWNTTeMs8YRT2jjIQU6r1cVoqe6tIf7bYg0RK', b'0', b'0', NULL, NULL, NULL, '2021-06-04 19:56:07', '2021-06-26 10:50:23');

-- ----------------------------
-- Table structure for ums_admins_jobs
-- ----------------------------
DROP TABLE IF EXISTS `ums_admins_jobs`;
CREATE TABLE `ums_admins_jobs`  (
  `admin_id` bigint(20) NOT NULL COMMENT '用户ID',
  `job_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`admin_id`, `job_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_admins_jobs
-- ----------------------------
INSERT INTO `ums_admins_jobs` VALUES (1, 11);
INSERT INTO `ums_admins_jobs` VALUES (2, 10);
INSERT INTO `ums_admins_jobs` VALUES (2, 12);
INSERT INTO `ums_admins_jobs` VALUES (3, 11);

-- ----------------------------
-- Table structure for ums_admins_roles
-- ----------------------------
DROP TABLE IF EXISTS `ums_admins_roles`;
CREATE TABLE `ums_admins_roles`  (
  `admin_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`admin_id`, `role_id`) USING BTREE,
  INDEX `FKq4eq273l04bpu4efj0jd0jb98`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_admins_roles
-- ----------------------------
INSERT INTO `ums_admins_roles` VALUES (1, 1);
INSERT INTO `ums_admins_roles` VALUES (2, 1);
INSERT INTO `ums_admins_roles` VALUES (2, 2);
INSERT INTO `ums_admins_roles` VALUES (3, 13);

-- ----------------------------
-- Table structure for ums_depts
-- ----------------------------
DROP TABLE IF EXISTS `ums_depts`;
CREATE TABLE `ums_depts`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '上级部门',
  `sub_count` int(5) NULL DEFAULT 0 COMMENT '子部门数目',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `dept_sort` int(5) NULL DEFAULT 999 COMMENT '排序',
  `enabled` bit(1) NOT NULL COMMENT '状态',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `inx_pid`(`pid`) USING BTREE,
  INDEX `inx_enabled`(`enabled`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_depts
-- ----------------------------
INSERT INTO `ums_depts` VALUES (2, 7, 1, '研发部', 3, b'1', 'admin', 'admin', '2019-03-25 09:15:32', '2020-08-02 14:48:47');
INSERT INTO `ums_depts` VALUES (5, 7, 0, '运维部', 4, b'1', 'admin', 'admin', '2019-03-25 09:20:44', '2020-05-17 14:27:27');
INSERT INTO `ums_depts` VALUES (6, 8, 0, '测试部', 6, b'1', 'admin', 'admin', '2019-03-25 09:52:18', '2020-06-08 11:59:21');
INSERT INTO `ums_depts` VALUES (7, 0, 2, '华南分部', 0, b'1', 'admin', 'admin', '2019-03-25 11:04:50', '2020-06-08 12:08:56');
INSERT INTO `ums_depts` VALUES (8, 0, 2, '华北分部', 1, b'1', 'admin', 'admin', '2019-03-25 11:04:53', '2020-05-14 12:54:00');
INSERT INTO `ums_depts` VALUES (15, 8, 1, 'UI部门', 7, b'1', 'admin', 'admin', '2020-05-13 22:56:53', '2020-05-14 12:54:13');
INSERT INTO `ums_depts` VALUES (17, 2, 0, '研发一组', 999, b'1', 'admin', 'admin', '2020-08-02 14:49:07', '2020-08-02 14:49:07');
INSERT INTO `ums_depts` VALUES (20, 15, 0, 'UI一组', 9, b'1', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for ums_dict_details
-- ----------------------------
DROP TABLE IF EXISTS `ums_dict_details`;
CREATE TABLE `ums_dict_details`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_id` bigint(11) NULL DEFAULT NULL COMMENT '字典id',
  `label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典标签',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典值',
  `dict_sort` int(5) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK5tpkputc6d9nboxojdbgnpmyb`(`dict_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典详情' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_dict_details
-- ----------------------------
INSERT INTO `ums_dict_details` VALUES (1, 1, '激活', 'true', 1, NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO `ums_dict_details` VALUES (2, 1, '禁用', 'false', 2, NULL, NULL, NULL, NULL);
INSERT INTO `ums_dict_details` VALUES (3, 4, '启用', 'true', 1, NULL, NULL, NULL, NULL);
INSERT INTO `ums_dict_details` VALUES (4, 4, '停用', 'false', 2, NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO `ums_dict_details` VALUES (5, 5, '启用', 'true', 1, NULL, NULL, NULL, NULL);
INSERT INTO `ums_dict_details` VALUES (6, 5, '停用', 'false', 2, NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO `ums_dict_details` VALUES (7, 6, '显示', '1', 1, NULL, NULL, NULL, NULL);
INSERT INTO `ums_dict_details` VALUES (8, 6, '隐藏', '0', 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for ums_dicts
-- ----------------------------
DROP TABLE IF EXISTS `ums_dicts`;
CREATE TABLE `ums_dicts`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_dicts
-- ----------------------------
INSERT INTO `ums_dicts` VALUES (1, 'user_status', '用户状态', NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO `ums_dicts` VALUES (4, 'dept_status', '部门状态', NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO `ums_dicts` VALUES (5, 'job_status', '岗位状态', NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO `ums_dicts` VALUES (6, 'show_status', '显示状态', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for ums_jobs
-- ----------------------------
DROP TABLE IF EXISTS `ums_jobs`;
CREATE TABLE `ums_jobs`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
  `enabled` bit(1) NOT NULL COMMENT '岗位状态',
  `job_sort` int(5) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name`) USING BTREE,
  INDEX `inx_enabled`(`enabled`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '岗位' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_jobs
-- ----------------------------
INSERT INTO `ums_jobs` VALUES (8, '人事专员', b'1', 3, NULL, NULL, '2019-03-29 14:52:28', NULL);
INSERT INTO `ums_jobs` VALUES (10, '产品经理', b'1', 4, NULL, NULL, '2019-03-29 14:55:51', NULL);
INSERT INTO `ums_jobs` VALUES (11, '全栈开发', b'1', 2, NULL, 'admin', '2019-03-31 13:39:30', '2020-05-05 11:33:43');
INSERT INTO `ums_jobs` VALUES (12, '软件测试', b'1', 5, NULL, 'admin', '2019-03-31 13:39:43', '2020-05-10 19:56:26');

-- ----------------------------
-- Table structure for ums_menus
-- ----------------------------
DROP TABLE IF EXISTS `ums_menus`;
CREATE TABLE `ums_menus`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(20) NULL DEFAULT 0 COMMENT '上级菜单ID',
  `sub_count` int(5) NULL DEFAULT 0 COMMENT '子菜单数目',
  `type` int(11) NULL DEFAULT NULL COMMENT '菜单类型',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名称',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
  `menu_sort` int(5) NULL DEFAULT NULL COMMENT '排序',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接地址',
  `i_frame` bit(1) NULL DEFAULT NULL COMMENT '是否外链',
  `cache` bit(1) NULL DEFAULT b'0' COMMENT '缓存',
  `hidden` bit(1) NULL DEFAULT b'0' COMMENT '隐藏',
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_title`(`title`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name`) USING BTREE,
  INDEX `inx_pid`(`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 123 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统菜单' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_menus
-- ----------------------------
INSERT INTO `ums_menus` VALUES (1, 0, 7, 0, '系统管理', NULL, NULL, 1, 'system', 'system', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-18 15:11:29', NULL);
INSERT INTO `ums_menus` VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', b'0', b'0', b'0', 'user:list', NULL, NULL, '2018-12-18 15:14:44', NULL);
INSERT INTO `ums_menus` VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/role/index', 3, 'role', 'role', b'0', b'0', b'0', 'roles:list', NULL, NULL, '2018-12-18 15:16:07', NULL);
INSERT INTO `ums_menus` VALUES (5, 1, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', b'0', b'0', b'0', 'menu:list', NULL, NULL, '2018-12-18 15:17:28', NULL);
INSERT INTO `ums_menus` VALUES (6, 0, 5, 0, '系统监控', NULL, NULL, 10, 'monitor', 'monitor', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-18 15:17:48', NULL);
INSERT INTO `ums_menus` VALUES (7, 6, 0, 1, '操作日志', 'Log', 'monitor/log/index', 11, 'log', 'logs', b'0', b'1', b'0', NULL, NULL, 'admin', '2018-12-18 15:18:26', '2020-06-06 13:11:57');
INSERT INTO `ums_menus` VALUES (9, 6, 0, 1, 'SQL监控', 'Sql', 'monitor/sql/index', 18, 'sqlMonitor', 'druid', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-18 15:19:34', NULL);
INSERT INTO `ums_menus` VALUES (10, 0, 5, 0, '组件管理', NULL, NULL, 50, 'zujian', 'components', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-19 13:38:16', NULL);
INSERT INTO `ums_menus` VALUES (11, 10, 0, 1, '图标库', 'Icons', 'components/icons/index', 51, 'icon', 'icon', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-19 13:38:49', NULL);
INSERT INTO `ums_menus` VALUES (14, 36, 0, 1, '邮件工具', 'Email', 'tools/email/index', 35, 'email', 'email', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-27 10:13:09', NULL);
INSERT INTO `ums_menus` VALUES (15, 10, 0, 1, '富文本', 'Editor', 'components/Editor', 52, 'fwb', 'tinymce', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-27 11:58:25', NULL);
INSERT INTO `ums_menus` VALUES (18, 36, 3, 1, '存储管理', 'Storage', 'tools/storage/index', 34, 'qiniu', 'storage', b'0', b'0', b'0', 'storage:list', NULL, NULL, '2018-12-31 11:12:15', NULL);
INSERT INTO `ums_menus` VALUES (19, 36, 0, 1, '支付宝工具', 'AliPay', 'tools/aliPay/index', 37, 'alipay', 'aliPay', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-31 14:52:38', NULL);
INSERT INTO `ums_menus` VALUES (21, 0, 2, 0, '多级菜单', NULL, '', 900, 'menu', 'nested', b'0', b'0', b'0', NULL, NULL, 'admin', '2019-01-04 16:22:03', '2020-06-21 17:27:35');
INSERT INTO `ums_menus` VALUES (22, 21, 2, 0, '二级菜单1', NULL, '', 999, 'menu', 'menu1', b'0', b'0', b'0', NULL, NULL, 'admin', '2019-01-04 16:23:29', '2020-06-21 17:27:20');
INSERT INTO `ums_menus` VALUES (23, 21, 0, 1, '二级菜单2', NULL, 'nested/menu2/index', 999, 'menu', 'menu2', b'0', b'0', b'0', NULL, NULL, NULL, '2019-01-04 16:23:57', NULL);
INSERT INTO `ums_menus` VALUES (24, 22, 0, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 999, 'menu', 'menu1-1', b'0', b'0', b'0', NULL, NULL, NULL, '2019-01-04 16:24:48', NULL);
INSERT INTO `ums_menus` VALUES (27, 22, 0, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 999, 'menu', 'menu1-2', b'0', b'0', b'0', NULL, NULL, NULL, '2019-01-07 17:27:32', NULL);
INSERT INTO `ums_menus` VALUES (28, 1, 3, 1, '任务调度', 'Timing', 'system/timing/index', 999, 'timing', 'timing', b'0', b'0', b'0', 'timing:list', NULL, NULL, '2019-01-07 20:34:40', NULL);
INSERT INTO `ums_menus` VALUES (30, 36, 0, 1, '代码生成', 'GeneratorIndex', 'generator/index', 32, 'dev', 'generator', b'0', b'1', b'0', NULL, NULL, NULL, '2019-01-11 15:45:55', NULL);
INSERT INTO `ums_menus` VALUES (32, 6, 0, 1, '异常日志', 'ErrorLog', 'monitor/log/errorLog', 12, 'error', 'errorLog', b'0', b'0', b'0', NULL, NULL, NULL, '2019-01-13 13:49:03', NULL);
INSERT INTO `ums_menus` VALUES (33, 10, 0, 1, 'Markdown', 'Markdown', 'components/MarkDown', 53, 'markdown', 'markdown', b'0', b'0', b'0', NULL, NULL, NULL, '2019-03-08 13:46:44', NULL);
INSERT INTO `ums_menus` VALUES (34, 10, 0, 1, 'Yaml编辑器', 'YamlEdit', 'components/YamlEdit', 54, 'dev', 'yaml', b'0', b'0', b'0', NULL, NULL, NULL, '2019-03-08 15:49:40', NULL);
INSERT INTO `ums_menus` VALUES (35, 1, 3, 1, '部门管理', 'Dept', 'system/dept/index', 6, 'dept', 'dept', b'0', b'0', b'0', 'dept:list', NULL, NULL, '2019-03-25 09:46:00', NULL);
INSERT INTO `ums_menus` VALUES (36, 0, 7, 0, '系统工具', NULL, '', 30, 'sys-tools', 'sys-tools', b'0', b'0', b'0', NULL, NULL, NULL, '2019-03-29 10:57:35', NULL);
INSERT INTO `ums_menus` VALUES (37, 1, 3, 1, '岗位管理', 'Job', 'system/job/index', 7, 'Steve-Jobs', 'job', b'0', b'0', b'0', 'job:list', NULL, NULL, '2019-03-29 13:51:18', NULL);
INSERT INTO `ums_menus` VALUES (38, 36, 0, 1, '接口文档', 'Swagger', 'tools/swagger/index', 36, 'swagger', 'swagger2', b'0', b'0', b'0', NULL, NULL, NULL, '2019-03-29 19:57:53', NULL);
INSERT INTO `ums_menus` VALUES (39, 1, 3, 1, '字典管理', 'Dict', 'system/dict/index', 8, 'dictionary', 'dict', b'0', b'0', b'0', 'dict:list', NULL, NULL, '2019-04-10 11:49:04', NULL);
INSERT INTO `ums_menus` VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'monitor/online/index', 10, 'Steve-Jobs', 'online', b'0', b'0', b'0', NULL, NULL, NULL, '2019-10-26 22:08:43', NULL);
INSERT INTO `ums_menus` VALUES (44, 2, 0, 2, '用户新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'user:add', NULL, NULL, '2019-10-29 10:59:46', NULL);
INSERT INTO `ums_menus` VALUES (45, 2, 0, 2, '用户编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'user:edit', NULL, NULL, '2019-10-29 11:00:08', NULL);
INSERT INTO `ums_menus` VALUES (46, 2, 0, 2, '用户删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'user:del', NULL, NULL, '2019-10-29 11:00:23', NULL);
INSERT INTO `ums_menus` VALUES (48, 3, 0, 2, '角色创建', NULL, '', 2, '', '', b'0', b'0', b'0', 'roles:add', NULL, NULL, '2019-10-29 12:45:34', NULL);
INSERT INTO `ums_menus` VALUES (49, 3, 0, 2, '角色修改', NULL, '', 3, '', '', b'0', b'0', b'0', 'roles:edit', NULL, NULL, '2019-10-29 12:46:16', NULL);
INSERT INTO `ums_menus` VALUES (50, 3, 0, 2, '角色删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'roles:del', NULL, NULL, '2019-10-29 12:46:51', NULL);
INSERT INTO `ums_menus` VALUES (52, 5, 0, 2, '菜单新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'menu:add', NULL, NULL, '2019-10-29 12:55:07', NULL);
INSERT INTO `ums_menus` VALUES (53, 5, 0, 2, '菜单编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'menu:edit', NULL, NULL, '2019-10-29 12:55:40', NULL);
INSERT INTO `ums_menus` VALUES (54, 5, 0, 2, '菜单删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'menu:del', NULL, NULL, '2019-10-29 12:56:00', NULL);
INSERT INTO `ums_menus` VALUES (56, 35, 0, 2, '部门新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dept:add', NULL, NULL, '2019-10-29 12:57:09', NULL);
INSERT INTO `ums_menus` VALUES (57, 35, 0, 2, '部门编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dept:edit', NULL, NULL, '2019-10-29 12:57:27', NULL);
INSERT INTO `ums_menus` VALUES (58, 35, 0, 2, '部门删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dept:del', NULL, NULL, '2019-10-29 12:57:41', NULL);
INSERT INTO `ums_menus` VALUES (60, 37, 0, 2, '岗位新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'job:add', NULL, NULL, '2019-10-29 12:58:27', NULL);
INSERT INTO `ums_menus` VALUES (61, 37, 0, 2, '岗位编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'job:edit', NULL, NULL, '2019-10-29 12:58:45', NULL);
INSERT INTO `ums_menus` VALUES (62, 37, 0, 2, '岗位删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'job:del', NULL, NULL, '2019-10-29 12:59:04', NULL);
INSERT INTO `ums_menus` VALUES (64, 39, 0, 2, '字典新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dict:add', NULL, NULL, '2019-10-29 13:00:17', NULL);
INSERT INTO `ums_menus` VALUES (65, 39, 0, 2, '字典编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dict:edit', NULL, NULL, '2019-10-29 13:00:42', NULL);
INSERT INTO `ums_menus` VALUES (66, 39, 0, 2, '字典删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dict:del', NULL, NULL, '2019-10-29 13:00:59', NULL);
INSERT INTO `ums_menus` VALUES (73, 28, 0, 2, '任务新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'timing:add', NULL, NULL, '2019-10-29 13:07:28', NULL);
INSERT INTO `ums_menus` VALUES (74, 28, 0, 2, '任务编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'timing:edit', NULL, NULL, '2019-10-29 13:07:41', NULL);
INSERT INTO `ums_menus` VALUES (75, 28, 0, 2, '任务删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'timing:del', NULL, NULL, '2019-10-29 13:07:54', NULL);
INSERT INTO `ums_menus` VALUES (77, 18, 0, 2, '上传文件', NULL, '', 2, '', '', b'0', b'0', b'0', 'storage:add', NULL, NULL, '2019-10-29 13:09:09', NULL);
INSERT INTO `ums_menus` VALUES (78, 18, 0, 2, '文件编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'storage:edit', NULL, NULL, '2019-10-29 13:09:22', NULL);
INSERT INTO `ums_menus` VALUES (79, 18, 0, 2, '文件删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'storage:del', NULL, NULL, '2019-10-29 13:09:34', NULL);
INSERT INTO `ums_menus` VALUES (80, 6, 0, 1, '服务监控', 'ServerMonitor', 'monitor/server/index', 14, 'codeConsole', 'server', b'0', b'0', b'0', 'monitor:list', NULL, 'admin', '2019-11-07 13:06:39', '2020-05-04 18:20:50');
INSERT INTO `ums_menus` VALUES (82, 36, 0, 1, '生成配置', 'GeneratorConfig', 'generator/config', 33, 'dev', 'generator/config/:tableName', b'0', b'1', b'1', '', NULL, NULL, '2019-11-17 20:08:56', NULL);
INSERT INTO `ums_menus` VALUES (83, 10, 0, 1, '图表库', 'Echarts', 'components/Echarts', 50, 'chart', 'echarts', b'0', b'1', b'0', '', NULL, NULL, '2019-11-21 09:04:32', NULL);
INSERT INTO `ums_menus` VALUES (90, 0, 5, 1, '运维管理', 'Mnt', '', 20, 'mnt', 'mnt', b'0', b'0', b'0', NULL, NULL, NULL, '2019-11-09 10:31:08', NULL);
INSERT INTO `ums_menus` VALUES (92, 90, 3, 1, '服务器', 'ServerDeploy', 'mnt/server/index', 22, 'server', 'mnt/serverDeploy', b'0', b'0', b'0', 'serverDeploy:list', NULL, NULL, '2019-11-10 10:29:25', NULL);
INSERT INTO `ums_menus` VALUES (93, 90, 3, 1, '应用管理', 'App', 'mnt/app/index', 23, 'app', 'mnt/app', b'0', b'0', b'0', 'app:list', NULL, NULL, '2019-11-10 11:05:16', NULL);
INSERT INTO `ums_menus` VALUES (94, 90, 3, 1, '部署管理', 'Deploy', 'mnt/deploy/index', 24, 'deploy', 'mnt/deploy', b'0', b'0', b'0', 'deploy:list', NULL, NULL, '2019-11-10 15:56:55', NULL);
INSERT INTO `ums_menus` VALUES (97, 90, 1, 1, '部署备份', 'DeployHistory', 'mnt/deployHistory/index', 25, 'backup', 'mnt/deployHistory', b'0', b'0', b'0', 'deployHistory:list', NULL, NULL, '2019-11-10 16:49:44', NULL);
INSERT INTO `ums_menus` VALUES (98, 90, 3, 1, '数据库管理', 'Database', 'mnt/database/index', 26, 'database', 'mnt/database', b'0', b'0', b'0', 'database:list', NULL, NULL, '2019-11-10 20:40:04', NULL);
INSERT INTO `ums_menus` VALUES (102, 97, 0, 2, '删除', NULL, '', 999, '', '', b'0', b'0', b'0', 'deployHistory:del', NULL, NULL, '2019-11-17 09:32:48', NULL);
INSERT INTO `ums_menus` VALUES (103, 92, 0, 2, '服务器新增', NULL, '', 999, '', '', b'0', b'0', b'0', 'serverDeploy:add', NULL, NULL, '2019-11-17 11:08:33', NULL);
INSERT INTO `ums_menus` VALUES (104, 92, 0, 2, '服务器编辑', NULL, '', 999, '', '', b'0', b'0', b'0', 'serverDeploy:edit', NULL, NULL, '2019-11-17 11:08:57', NULL);
INSERT INTO `ums_menus` VALUES (105, 92, 0, 2, '服务器删除', NULL, '', 999, '', '', b'0', b'0', b'0', 'serverDeploy:del', NULL, NULL, '2019-11-17 11:09:15', NULL);
INSERT INTO `ums_menus` VALUES (106, 93, 0, 2, '应用新增', NULL, '', 999, '', '', b'0', b'0', b'0', 'app:add', NULL, NULL, '2019-11-17 11:10:03', NULL);
INSERT INTO `ums_menus` VALUES (107, 93, 0, 2, '应用编辑', NULL, '', 999, '', '', b'0', b'0', b'0', 'app:edit', NULL, NULL, '2019-11-17 11:10:28', NULL);
INSERT INTO `ums_menus` VALUES (108, 93, 0, 2, '应用删除', NULL, '', 999, '', '', b'0', b'0', b'0', 'app:del', NULL, NULL, '2019-11-17 11:10:55', NULL);
INSERT INTO `ums_menus` VALUES (109, 94, 0, 2, '部署新增', NULL, '', 999, '', '', b'0', b'0', b'0', 'deploy:add', NULL, NULL, '2019-11-17 11:11:22', NULL);
INSERT INTO `ums_menus` VALUES (110, 94, 0, 2, '部署编辑', NULL, '', 999, '', '', b'0', b'0', b'0', 'deploy:edit', NULL, NULL, '2019-11-17 11:11:41', NULL);
INSERT INTO `ums_menus` VALUES (111, 94, 0, 2, '部署删除', NULL, '', 999, '', '', b'0', b'0', b'0', 'deploy:del', NULL, NULL, '2019-11-17 11:12:01', NULL);
INSERT INTO `ums_menus` VALUES (112, 98, 0, 2, '数据库新增', NULL, '', 999, '', '', b'0', b'0', b'0', 'database:add', NULL, NULL, '2019-11-17 11:12:43', NULL);
INSERT INTO `ums_menus` VALUES (113, 98, 0, 2, '数据库编辑', NULL, '', 999, '', '', b'0', b'0', b'0', 'database:edit', NULL, NULL, '2019-11-17 11:12:58', NULL);
INSERT INTO `ums_menus` VALUES (114, 98, 0, 2, '数据库删除', NULL, '', 999, '', '', b'0', b'0', b'0', 'database:del', NULL, NULL, '2019-11-17 11:13:14', NULL);
INSERT INTO `ums_menus` VALUES (116, 36, 0, 1, '生成预览', 'Preview', 'generator/preview', 999, 'java', 'generator/preview/:tableName', b'0', b'1', b'1', NULL, NULL, NULL, '2019-11-26 14:54:36', NULL);
INSERT INTO `ums_menus` VALUES (119, 117, 0, 1, '子菜单01', NULL, '/user', 999, 'zujian', '/user', b'0', b'0', b'0', '/user:list', NULL, NULL, '2021-06-11 12:27:16', '2021-06-11 12:27:16');
INSERT INTO `ums_menus` VALUES (120, 0, 2, 0, '商品管理', NULL, NULL, 999, 'product', 'product', b'0', b'0', b'0', NULL, NULL, NULL, '2021-06-24 22:12:29', '2021-06-24 22:51:12');
INSERT INTO `ums_menus` VALUES (121, 120, 0, 1, '商品列表', NULL, 'product/product/index', 1, 'list', '/product/list', b'0', b'0', b'0', 'product:list', NULL, NULL, '2021-06-24 22:17:14', '2021-06-24 22:54:24');
INSERT INTO `ums_menus` VALUES (122, 120, 0, 1, '商品分类', NULL, 'product/category/index', 2, 'category', '/product/category', b'0', b'0', b'0', 'product-category:list', NULL, NULL, '2021-06-24 22:51:12', '2021-06-24 22:55:48');

-- ----------------------------
-- Table structure for ums_roles
-- ----------------------------
DROP TABLE IF EXISTS `ums_roles`;
CREATE TABLE `ums_roles`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `level` int(255) NULL DEFAULT NULL COMMENT '角色级别',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `data_scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据权限',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name`) USING BTREE,
  INDEX `role_name_index`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_roles
-- ----------------------------
INSERT INTO `ums_roles` VALUES (1, '超级管理员', 1, '-', '全部', NULL, 'admin', '2018-11-23 11:04:37', '2020-08-06 16:10:24');
INSERT INTO `ums_roles` VALUES (2, '普通用户', 2, '-', '本级', NULL, 'admin', '2018-11-23 13:09:06', '2020-09-05 10:45:12');
INSERT INTO `ums_roles` VALUES (13, 'user01', 3, 'user 01', '自定义', NULL, NULL, '2021-06-07 14:47:19', '2021-06-20 05:41:33');
INSERT INTO `ums_roles` VALUES (14, 'user02', 3, 'user02', '本级', NULL, NULL, '2021-06-07 14:54:01', '2021-06-07 14:54:01');
INSERT INTO `ums_roles` VALUES (15, 'user04', 3, 'user04 描述', '自定义', NULL, NULL, '2021-06-07 14:59:11', '2021-06-08 12:58:15');

-- ----------------------------
-- Table structure for ums_roles_depts
-- ----------------------------
DROP TABLE IF EXISTS `ums_roles_depts`;
CREATE TABLE `ums_roles_depts`  (
  `role_id` bigint(20) NOT NULL,
  `dept_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE,
  INDEX `FK7qg6itn5ajdoa9h9o78v9ksur`(`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色部门关联' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_roles_depts
-- ----------------------------
INSERT INTO `ums_roles_depts` VALUES (15, 6);
INSERT INTO `ums_roles_depts` VALUES (13, 8);

-- ----------------------------
-- Table structure for ums_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS `ums_roles_menus`;
CREATE TABLE `ums_roles_menus`  (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`menu_id`, `role_id`) USING BTREE,
  INDEX `FKcngg2qadojhi3a651a5adkvbq`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单关联' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_roles_menus
-- ----------------------------
INSERT INTO `ums_roles_menus` VALUES (1, 1);
INSERT INTO `ums_roles_menus` VALUES (2, 1);
INSERT INTO `ums_roles_menus` VALUES (3, 1);
INSERT INTO `ums_roles_menus` VALUES (5, 1);
INSERT INTO `ums_roles_menus` VALUES (6, 1);
INSERT INTO `ums_roles_menus` VALUES (7, 1);
INSERT INTO `ums_roles_menus` VALUES (9, 1);
INSERT INTO `ums_roles_menus` VALUES (10, 1);
INSERT INTO `ums_roles_menus` VALUES (11, 1);
INSERT INTO `ums_roles_menus` VALUES (14, 1);
INSERT INTO `ums_roles_menus` VALUES (15, 1);
INSERT INTO `ums_roles_menus` VALUES (18, 1);
INSERT INTO `ums_roles_menus` VALUES (19, 1);
INSERT INTO `ums_roles_menus` VALUES (21, 1);
INSERT INTO `ums_roles_menus` VALUES (22, 1);
INSERT INTO `ums_roles_menus` VALUES (23, 1);
INSERT INTO `ums_roles_menus` VALUES (24, 1);
INSERT INTO `ums_roles_menus` VALUES (27, 1);
INSERT INTO `ums_roles_menus` VALUES (28, 1);
INSERT INTO `ums_roles_menus` VALUES (30, 1);
INSERT INTO `ums_roles_menus` VALUES (32, 1);
INSERT INTO `ums_roles_menus` VALUES (33, 1);
INSERT INTO `ums_roles_menus` VALUES (34, 1);
INSERT INTO `ums_roles_menus` VALUES (35, 1);
INSERT INTO `ums_roles_menus` VALUES (36, 1);
INSERT INTO `ums_roles_menus` VALUES (37, 1);
INSERT INTO `ums_roles_menus` VALUES (38, 1);
INSERT INTO `ums_roles_menus` VALUES (39, 1);
INSERT INTO `ums_roles_menus` VALUES (41, 1);
INSERT INTO `ums_roles_menus` VALUES (44, 1);
INSERT INTO `ums_roles_menus` VALUES (45, 1);
INSERT INTO `ums_roles_menus` VALUES (46, 1);
INSERT INTO `ums_roles_menus` VALUES (48, 1);
INSERT INTO `ums_roles_menus` VALUES (49, 1);
INSERT INTO `ums_roles_menus` VALUES (50, 1);
INSERT INTO `ums_roles_menus` VALUES (52, 1);
INSERT INTO `ums_roles_menus` VALUES (53, 1);
INSERT INTO `ums_roles_menus` VALUES (54, 1);
INSERT INTO `ums_roles_menus` VALUES (56, 1);
INSERT INTO `ums_roles_menus` VALUES (57, 1);
INSERT INTO `ums_roles_menus` VALUES (58, 1);
INSERT INTO `ums_roles_menus` VALUES (60, 1);
INSERT INTO `ums_roles_menus` VALUES (61, 1);
INSERT INTO `ums_roles_menus` VALUES (62, 1);
INSERT INTO `ums_roles_menus` VALUES (64, 1);
INSERT INTO `ums_roles_menus` VALUES (65, 1);
INSERT INTO `ums_roles_menus` VALUES (66, 1);
INSERT INTO `ums_roles_menus` VALUES (73, 1);
INSERT INTO `ums_roles_menus` VALUES (74, 1);
INSERT INTO `ums_roles_menus` VALUES (75, 1);
INSERT INTO `ums_roles_menus` VALUES (77, 1);
INSERT INTO `ums_roles_menus` VALUES (78, 1);
INSERT INTO `ums_roles_menus` VALUES (79, 1);
INSERT INTO `ums_roles_menus` VALUES (80, 1);
INSERT INTO `ums_roles_menus` VALUES (82, 1);
INSERT INTO `ums_roles_menus` VALUES (83, 1);
INSERT INTO `ums_roles_menus` VALUES (90, 1);
INSERT INTO `ums_roles_menus` VALUES (92, 1);
INSERT INTO `ums_roles_menus` VALUES (93, 1);
INSERT INTO `ums_roles_menus` VALUES (94, 1);
INSERT INTO `ums_roles_menus` VALUES (97, 1);
INSERT INTO `ums_roles_menus` VALUES (98, 1);
INSERT INTO `ums_roles_menus` VALUES (102, 1);
INSERT INTO `ums_roles_menus` VALUES (103, 1);
INSERT INTO `ums_roles_menus` VALUES (104, 1);
INSERT INTO `ums_roles_menus` VALUES (105, 1);
INSERT INTO `ums_roles_menus` VALUES (106, 1);
INSERT INTO `ums_roles_menus` VALUES (107, 1);
INSERT INTO `ums_roles_menus` VALUES (108, 1);
INSERT INTO `ums_roles_menus` VALUES (109, 1);
INSERT INTO `ums_roles_menus` VALUES (110, 1);
INSERT INTO `ums_roles_menus` VALUES (111, 1);
INSERT INTO `ums_roles_menus` VALUES (112, 1);
INSERT INTO `ums_roles_menus` VALUES (113, 1);
INSERT INTO `ums_roles_menus` VALUES (114, 1);
INSERT INTO `ums_roles_menus` VALUES (116, 1);
INSERT INTO `ums_roles_menus` VALUES (120, 1);
INSERT INTO `ums_roles_menus` VALUES (121, 1);
INSERT INTO `ums_roles_menus` VALUES (122, 1);
INSERT INTO `ums_roles_menus` VALUES (1, 2);
INSERT INTO `ums_roles_menus` VALUES (2, 2);
INSERT INTO `ums_roles_menus` VALUES (5, 2);
INSERT INTO `ums_roles_menus` VALUES (6, 2);
INSERT INTO `ums_roles_menus` VALUES (7, 2);
INSERT INTO `ums_roles_menus` VALUES (9, 2);
INSERT INTO `ums_roles_menus` VALUES (10, 2);
INSERT INTO `ums_roles_menus` VALUES (11, 2);
INSERT INTO `ums_roles_menus` VALUES (15, 2);
INSERT INTO `ums_roles_menus` VALUES (21, 2);
INSERT INTO `ums_roles_menus` VALUES (22, 2);
INSERT INTO `ums_roles_menus` VALUES (23, 2);
INSERT INTO `ums_roles_menus` VALUES (24, 2);
INSERT INTO `ums_roles_menus` VALUES (27, 2);
INSERT INTO `ums_roles_menus` VALUES (32, 2);
INSERT INTO `ums_roles_menus` VALUES (33, 2);
INSERT INTO `ums_roles_menus` VALUES (34, 2);
INSERT INTO `ums_roles_menus` VALUES (80, 2);
INSERT INTO `ums_roles_menus` VALUES (83, 2);
INSERT INTO `ums_roles_menus` VALUES (1, 14);
INSERT INTO `ums_roles_menus` VALUES (2, 14);
INSERT INTO `ums_roles_menus` VALUES (3, 14);
INSERT INTO `ums_roles_menus` VALUES (5, 14);
INSERT INTO `ums_roles_menus` VALUES (6, 14);
INSERT INTO `ums_roles_menus` VALUES (7, 14);
INSERT INTO `ums_roles_menus` VALUES (9, 14);
INSERT INTO `ums_roles_menus` VALUES (10, 14);
INSERT INTO `ums_roles_menus` VALUES (11, 14);
INSERT INTO `ums_roles_menus` VALUES (14, 14);
INSERT INTO `ums_roles_menus` VALUES (15, 14);
INSERT INTO `ums_roles_menus` VALUES (18, 14);
INSERT INTO `ums_roles_menus` VALUES (19, 14);
INSERT INTO `ums_roles_menus` VALUES (21, 14);
INSERT INTO `ums_roles_menus` VALUES (22, 14);
INSERT INTO `ums_roles_menus` VALUES (23, 14);
INSERT INTO `ums_roles_menus` VALUES (24, 14);
INSERT INTO `ums_roles_menus` VALUES (27, 14);
INSERT INTO `ums_roles_menus` VALUES (28, 14);
INSERT INTO `ums_roles_menus` VALUES (30, 14);
INSERT INTO `ums_roles_menus` VALUES (32, 14);
INSERT INTO `ums_roles_menus` VALUES (33, 14);
INSERT INTO `ums_roles_menus` VALUES (34, 14);
INSERT INTO `ums_roles_menus` VALUES (35, 14);
INSERT INTO `ums_roles_menus` VALUES (36, 14);
INSERT INTO `ums_roles_menus` VALUES (37, 14);
INSERT INTO `ums_roles_menus` VALUES (38, 14);
INSERT INTO `ums_roles_menus` VALUES (39, 14);
INSERT INTO `ums_roles_menus` VALUES (41, 14);
INSERT INTO `ums_roles_menus` VALUES (44, 14);
INSERT INTO `ums_roles_menus` VALUES (45, 14);
INSERT INTO `ums_roles_menus` VALUES (46, 14);
INSERT INTO `ums_roles_menus` VALUES (48, 14);
INSERT INTO `ums_roles_menus` VALUES (49, 14);
INSERT INTO `ums_roles_menus` VALUES (50, 14);
INSERT INTO `ums_roles_menus` VALUES (52, 14);
INSERT INTO `ums_roles_menus` VALUES (53, 14);
INSERT INTO `ums_roles_menus` VALUES (54, 14);
INSERT INTO `ums_roles_menus` VALUES (56, 14);
INSERT INTO `ums_roles_menus` VALUES (57, 14);
INSERT INTO `ums_roles_menus` VALUES (58, 14);
INSERT INTO `ums_roles_menus` VALUES (60, 14);
INSERT INTO `ums_roles_menus` VALUES (61, 14);
INSERT INTO `ums_roles_menus` VALUES (62, 14);
INSERT INTO `ums_roles_menus` VALUES (64, 14);
INSERT INTO `ums_roles_menus` VALUES (65, 14);
INSERT INTO `ums_roles_menus` VALUES (66, 14);
INSERT INTO `ums_roles_menus` VALUES (73, 14);
INSERT INTO `ums_roles_menus` VALUES (74, 14);
INSERT INTO `ums_roles_menus` VALUES (75, 14);
INSERT INTO `ums_roles_menus` VALUES (77, 14);
INSERT INTO `ums_roles_menus` VALUES (78, 14);
INSERT INTO `ums_roles_menus` VALUES (79, 14);
INSERT INTO `ums_roles_menus` VALUES (80, 14);
INSERT INTO `ums_roles_menus` VALUES (82, 14);
INSERT INTO `ums_roles_menus` VALUES (83, 14);
INSERT INTO `ums_roles_menus` VALUES (90, 14);
INSERT INTO `ums_roles_menus` VALUES (92, 14);
INSERT INTO `ums_roles_menus` VALUES (93, 14);
INSERT INTO `ums_roles_menus` VALUES (94, 14);
INSERT INTO `ums_roles_menus` VALUES (97, 14);
INSERT INTO `ums_roles_menus` VALUES (98, 14);
INSERT INTO `ums_roles_menus` VALUES (102, 14);
INSERT INTO `ums_roles_menus` VALUES (103, 14);
INSERT INTO `ums_roles_menus` VALUES (104, 14);
INSERT INTO `ums_roles_menus` VALUES (105, 14);
INSERT INTO `ums_roles_menus` VALUES (106, 14);
INSERT INTO `ums_roles_menus` VALUES (107, 14);
INSERT INTO `ums_roles_menus` VALUES (108, 14);
INSERT INTO `ums_roles_menus` VALUES (109, 14);
INSERT INTO `ums_roles_menus` VALUES (110, 14);
INSERT INTO `ums_roles_menus` VALUES (111, 14);
INSERT INTO `ums_roles_menus` VALUES (112, 14);
INSERT INTO `ums_roles_menus` VALUES (113, 14);
INSERT INTO `ums_roles_menus` VALUES (114, 14);
INSERT INTO `ums_roles_menus` VALUES (116, 14);
INSERT INTO `ums_roles_menus` VALUES (1, 15);
INSERT INTO `ums_roles_menus` VALUES (2, 15);
INSERT INTO `ums_roles_menus` VALUES (3, 15);
INSERT INTO `ums_roles_menus` VALUES (5, 15);
INSERT INTO `ums_roles_menus` VALUES (6, 15);
INSERT INTO `ums_roles_menus` VALUES (7, 15);
INSERT INTO `ums_roles_menus` VALUES (9, 15);
INSERT INTO `ums_roles_menus` VALUES (10, 15);
INSERT INTO `ums_roles_menus` VALUES (11, 15);
INSERT INTO `ums_roles_menus` VALUES (14, 15);
INSERT INTO `ums_roles_menus` VALUES (15, 15);
INSERT INTO `ums_roles_menus` VALUES (18, 15);
INSERT INTO `ums_roles_menus` VALUES (19, 15);
INSERT INTO `ums_roles_menus` VALUES (21, 15);
INSERT INTO `ums_roles_menus` VALUES (22, 15);
INSERT INTO `ums_roles_menus` VALUES (23, 15);
INSERT INTO `ums_roles_menus` VALUES (24, 15);
INSERT INTO `ums_roles_menus` VALUES (27, 15);
INSERT INTO `ums_roles_menus` VALUES (28, 15);
INSERT INTO `ums_roles_menus` VALUES (30, 15);
INSERT INTO `ums_roles_menus` VALUES (32, 15);
INSERT INTO `ums_roles_menus` VALUES (33, 15);
INSERT INTO `ums_roles_menus` VALUES (34, 15);
INSERT INTO `ums_roles_menus` VALUES (35, 15);
INSERT INTO `ums_roles_menus` VALUES (36, 15);
INSERT INTO `ums_roles_menus` VALUES (37, 15);
INSERT INTO `ums_roles_menus` VALUES (38, 15);
INSERT INTO `ums_roles_menus` VALUES (39, 15);
INSERT INTO `ums_roles_menus` VALUES (41, 15);
INSERT INTO `ums_roles_menus` VALUES (44, 15);
INSERT INTO `ums_roles_menus` VALUES (45, 15);
INSERT INTO `ums_roles_menus` VALUES (46, 15);
INSERT INTO `ums_roles_menus` VALUES (48, 15);
INSERT INTO `ums_roles_menus` VALUES (49, 15);
INSERT INTO `ums_roles_menus` VALUES (50, 15);
INSERT INTO `ums_roles_menus` VALUES (52, 15);
INSERT INTO `ums_roles_menus` VALUES (53, 15);
INSERT INTO `ums_roles_menus` VALUES (54, 15);
INSERT INTO `ums_roles_menus` VALUES (56, 15);
INSERT INTO `ums_roles_menus` VALUES (57, 15);
INSERT INTO `ums_roles_menus` VALUES (58, 15);
INSERT INTO `ums_roles_menus` VALUES (60, 15);
INSERT INTO `ums_roles_menus` VALUES (61, 15);
INSERT INTO `ums_roles_menus` VALUES (62, 15);
INSERT INTO `ums_roles_menus` VALUES (64, 15);
INSERT INTO `ums_roles_menus` VALUES (65, 15);
INSERT INTO `ums_roles_menus` VALUES (66, 15);
INSERT INTO `ums_roles_menus` VALUES (73, 15);
INSERT INTO `ums_roles_menus` VALUES (74, 15);
INSERT INTO `ums_roles_menus` VALUES (75, 15);
INSERT INTO `ums_roles_menus` VALUES (77, 15);
INSERT INTO `ums_roles_menus` VALUES (78, 15);
INSERT INTO `ums_roles_menus` VALUES (79, 15);
INSERT INTO `ums_roles_menus` VALUES (80, 15);
INSERT INTO `ums_roles_menus` VALUES (82, 15);
INSERT INTO `ums_roles_menus` VALUES (83, 15);
INSERT INTO `ums_roles_menus` VALUES (90, 15);
INSERT INTO `ums_roles_menus` VALUES (92, 15);
INSERT INTO `ums_roles_menus` VALUES (93, 15);
INSERT INTO `ums_roles_menus` VALUES (94, 15);
INSERT INTO `ums_roles_menus` VALUES (97, 15);
INSERT INTO `ums_roles_menus` VALUES (98, 15);
INSERT INTO `ums_roles_menus` VALUES (102, 15);
INSERT INTO `ums_roles_menus` VALUES (103, 15);
INSERT INTO `ums_roles_menus` VALUES (104, 15);
INSERT INTO `ums_roles_menus` VALUES (105, 15);
INSERT INTO `ums_roles_menus` VALUES (106, 15);
INSERT INTO `ums_roles_menus` VALUES (107, 15);
INSERT INTO `ums_roles_menus` VALUES (108, 15);
INSERT INTO `ums_roles_menus` VALUES (109, 15);
INSERT INTO `ums_roles_menus` VALUES (110, 15);
INSERT INTO `ums_roles_menus` VALUES (111, 15);
INSERT INTO `ums_roles_menus` VALUES (112, 15);
INSERT INTO `ums_roles_menus` VALUES (113, 15);
INSERT INTO `ums_roles_menus` VALUES (114, 15);
INSERT INTO `ums_roles_menus` VALUES (116, 15);

SET FOREIGN_KEY_CHECKS = 1;
