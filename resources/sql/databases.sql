/*
 Navicat Premium Data Transfer

 Source Server         : local-docker
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:33060
 Source Schema         : chdt

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 23/06/2021 14:06:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
INSERT INTO `ums_admins` VALUES (3, 17, 'user01', 'user01', 'F', '15211111111', 'user01@test.com', NULL, NULL, '$2a$10$eYcLbbujbRyIXPpweWNTTeMs8YRT2jjIQU6r1cVoqe6tIf7bYg0RK', b'0', b'1', NULL, NULL, NULL, '2021-06-04 19:56:07', '2021-06-18 06:21:27');

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典详情' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_dict_details
-- ----------------------------
INSERT INTO `ums_dict_details` VALUES (1, 1, '激活', 'true', 1, NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO `ums_dict_details` VALUES (2, 1, '禁用', 'false', 2, NULL, NULL, NULL, NULL);
INSERT INTO `ums_dict_details` VALUES (3, 4, '启用', 'true', 1, NULL, NULL, NULL, NULL);
INSERT INTO `ums_dict_details` VALUES (4, 4, '停用', 'false', 2, NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO `ums_dict_details` VALUES (5, 5, '启用', 'true', 1, NULL, NULL, NULL, NULL);
INSERT INTO `ums_dict_details` VALUES (6, 5, '停用', 'false', 2, NULL, NULL, '2019-10-27 20:31:36', NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ums_dicts
-- ----------------------------
INSERT INTO `ums_dicts` VALUES (1, 'user_status', '用户状态', NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO `ums_dicts` VALUES (4, 'dept_status', '部门状态', NULL, NULL, '2019-10-27 20:31:36', NULL);
INSERT INTO `ums_dicts` VALUES (5, 'job_status', '岗位状态', NULL, NULL, '2019-10-27 20:31:36', NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 120 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统菜单' ROW_FORMAT = Compact;

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
