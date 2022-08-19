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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of log_login
-- ----------------------------

-- ----------------------------
-- Table structure for r_grade_class
-- ----------------------------
DROP TABLE IF EXISTS `r_grade_class`;
CREATE TABLE `r_grade_class`  (
  `grade_id` int NOT NULL COMMENT '年级 ID',
  `class_id` int NOT NULL COMMENT '班级 ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_grade_class
-- ----------------------------

-- ----------------------------
-- Table structure for r_school_grade
-- ----------------------------
DROP TABLE IF EXISTS `r_school_grade`;
CREATE TABLE `r_school_grade`  (
  `school_id` int NOT NULL COMMENT '学校 ID',
  `grade_id` int NOT NULL COMMENT '年级 ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of r_school_grade
-- ----------------------------

-- ----------------------------
-- Table structure for t_class
-- ----------------------------
DROP TABLE IF EXISTS `t_class`;
CREATE TABLE `t_class`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '班级 ID',
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_class
-- ----------------------------
INSERT INTO `t_class` VALUES (0, '');

-- ----------------------------
-- Table structure for t_grade
-- ----------------------------
DROP TABLE IF EXISTS `t_grade`;
CREATE TABLE `t_grade`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '年级 ID',
  `grade` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年级名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_grade
-- ----------------------------
INSERT INTO `t_grade` VALUES (0, '');

-- ----------------------------
-- Table structure for t_school
-- ----------------------------
DROP TABLE IF EXISTS `t_school`;
CREATE TABLE `t_school`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '学校 ID',
  `school` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学校名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `school_uk_school_name`(`school` ASC) USING BTREE COMMENT '学校唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_school
-- ----------------------------
INSERT INTO `t_school` VALUES (0, '');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_upload
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (0, 'root', '0fe067dba5a97e46939e21a33b50dd99', '18327217494', 'springbear2020@163.com', 5, 0, '李春雄', '0', 0, 0, 0, '2022-06-02 00:00:00', '2022-06-02 00:00:00');

SET FOREIGN_KEY_CHECKS = 1;
