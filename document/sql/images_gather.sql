/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : images_gather

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 18/08/2022 23:33:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `images_gather`;

USE `images_gather`;

-- ----------------------------
-- Table structure for log_email
-- ----------------------------
DROP TABLE IF EXISTS `log_email`;
CREATE TABLE `log_email`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '邮件发送记录 ID',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目的邮箱地址',
  `code` char(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '验证码',
  `delivery_datetime` datetime NOT NULL COMMENT '发送时间',
  `user_id` int NOT NULL COMMENT '用户 ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of log_email
-- ----------------------------

-- ----------------------------
-- Table structure for log_login
-- ----------------------------
DROP TABLE IF EXISTS `log_login`;
CREATE TABLE `log_login`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '登录记录 ID',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'IP 地址',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录地点',
  `login_datetime` datetime NOT NULL COMMENT '上次登录时间',
  `user_id` int NOT NULL COMMENT '用户 ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of log_login
-- ----------------------------
INSERT INTO `log_login` VALUES (1, '127.0.0.1', '未知地点', '2023-07-02 18:24:42', 1);
INSERT INTO `log_login` VALUES (2, '127.0.0.1', '未知地点', '2023-07-02 18:25:56', 2);
INSERT INTO `log_login` VALUES (3, '127.0.0.1', '未知地点', '2023-07-02 18:36:26', 3);
INSERT INTO `log_login` VALUES (4, '127.0.0.1', '未知地点', '2023-07-02 18:36:58', 1);
INSERT INTO `log_login` VALUES (5, '127.0.0.1', '未知地点', '2023-07-02 18:37:48', 3);
INSERT INTO `log_login` VALUES (6, '127.0.0.1', '未知地点', '2023-07-02 18:39:44', 4);
INSERT INTO `log_login` VALUES (7, '127.0.0.1', '未知地点', '2023-07-02 18:40:52', 5);
INSERT INTO `log_login` VALUES (8, '127.0.0.1', '未知地点', '2023-07-02 18:48:33', 2);
INSERT INTO `log_login` VALUES (9, '127.0.0.1', '未知地点', '2023-07-02 18:52:24', 2);
INSERT INTO `log_login` VALUES (10, '127.0.0.1', '未知地点', '2023-07-02 18:53:54', 3);
INSERT INTO `log_login` VALUES (11, '127.0.0.1', '未知地点', '2023-07-02 18:55:12', 5);
INSERT INTO `log_login` VALUES (12, '127.0.0.1', '未知地点', '2023-07-02 18:56:33', 3);
INSERT INTO `log_login` VALUES (13, '127.0.0.1', '未知地点', '2023-07-02 18:57:01', 5);

-- ----------------------------
-- Table structure for r_grade_class
-- ----------------------------
DROP TABLE IF EXISTS `r_grade_class`;
CREATE TABLE `r_grade_class`  (
  `grade_id` int NOT NULL COMMENT '年级 ID',
  `class_id` int NOT NULL COMMENT '班级 ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of r_grade_class
-- ----------------------------
INSERT INTO `r_grade_class` VALUES (1, 1);

-- ----------------------------
-- Table structure for r_school_grade
-- ----------------------------
DROP TABLE IF EXISTS `r_school_grade`;
CREATE TABLE `r_school_grade`  (
  `school_id` int NOT NULL COMMENT '学校 ID',
  `grade_id` int NOT NULL COMMENT '年级 ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of r_school_grade
-- ----------------------------
INSERT INTO `r_school_grade` VALUES (1, 1);

-- ----------------------------
-- Table structure for t_class
-- ----------------------------
DROP TABLE IF EXISTS `t_class`;
CREATE TABLE `t_class`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '班级 ID',
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_class
-- ----------------------------
INSERT INTO `t_class` VALUES (1, '软件1901');

-- ----------------------------
-- Table structure for t_grade
-- ----------------------------
DROP TABLE IF EXISTS `t_grade`;
CREATE TABLE `t_grade`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '年级 ID',
  `grade` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年级名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_grade
-- ----------------------------
INSERT INTO `t_grade` VALUES (1, '2019');

-- ----------------------------
-- Table structure for t_school
-- ----------------------------
DROP TABLE IF EXISTS `t_school`;
CREATE TABLE `t_school`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '学校 ID',
  `school` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学校名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `school_uk_school_name`(`school` ASC) USING BTREE COMMENT '学校唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_school
-- ----------------------------
INSERT INTO `t_school` VALUES (1, '清华大学');

-- ----------------------------
-- Table structure for t_upload
-- ----------------------------
DROP TABLE IF EXISTS `t_upload`;
CREATE TABLE `t_upload`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '上传记录 ID',
  `upload_status` int NOT NULL COMMENT '今日是否完成上传',
  `upload_datetime` datetime NOT NULL COMMENT '上传时间',
  `local_health_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '本地健康码地址',
  `local_schedule_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '本地行程码地址',
  `local_closed_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '本地密接查地址',
  `cloud_health_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '云健康码地址',
  `cloud_schedule_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '云行程码地址',
  `cloud_closed_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '云密接查地址',
  `user_id` int NOT NULL COMMENT '用户 ID',
  `create_datetime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_upload
-- ----------------------------
INSERT INTO `t_upload` VALUES (3, 0, '2023-07-02 18:52:31', 'images-gather/清华大学/2019/软件1901/2023-07-02/张三-健康码-20230702185230.png', 'images-gather/清华大学/2019/软件1901/2023-07-02/张三-行程码-20230702185230.png', 'images-gather/清华大学/2019/软件1901/2023-07-02/张三-密接查-20230702185230.png', '', '', '', 2, '2023-07-02 18:52:24');
INSERT INTO `t_upload` VALUES (4, 1, '2023-07-02 18:56:33', '', '', '', '', '', '', 3, '2023-07-02 18:56:33');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户 ID',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `user_type` int NOT NULL COMMENT '用户类型',
  `user_status` int NOT NULL COMMENT '用户状态',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `sex` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '性别',
  `school_id` int NOT NULL COMMENT '学校 ID',
  `grade_id` int NOT NULL COMMENT '年级 ID',
  `class_id` int NOT NULL COMMENT '班级 ID',
  `last_login_datetime` datetime NOT NULL COMMENT '上次登录时间',
  `create_datetime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_uk_username`(`username` ASC) USING BTREE COMMENT '用户名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'admin', '4adb2e567e097fdcd0b88e15a435004a', '13598452561', 'springbear2020@163.com', 5, 0, '管理员', '男', 0, 0, 0, '2023-07-02 18:36:58', '2022-06-02 00:00:00');
INSERT INTO `t_user` VALUES (2, '10086', '270e8052c55383b71d5bc79976ea4c67', '13598452658', 'zhangsan@163.com', 1, 0, '张三', '男', 1, 1, 1, '2023-07-02 18:52:24', '2023-07-02 18:25:14');
INSERT INTO `t_user` VALUES (3, '10087', '5e66f963d6c641e41bf08b7bd1713db7', '18745862549', 'lisi@qq.com', 2, 0, '李四', '女', 1, 1, 1, '2023-07-02 18:56:33', '2023-07-02 18:25:14');
INSERT INTO `t_user` VALUES (4, '10088', '9a576ec571073a85e211799e38537849', '17658594256', 'wangwu@gmail.com', 3, 0, '王五', '女', 1, 1, 1, '2023-07-02 18:39:44', '2023-07-02 18:25:14');
INSERT INTO `t_user` VALUES (5, '10089', '84be8f888b072e3cf404feef8a41cef8', '1558452689', 'zhaoliu@outlook.com', 4, 0, '赵六', '女', 1, 1, 0, '2023-07-02 18:57:01', '2023-07-02 18:37:24');

SET FOREIGN_KEY_CHECKS = 1;
