/*
 Navicat Premium Dump SQL

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : students

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 03/06/2026 15:01:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mess
-- ----------------------------
DROP TABLE IF EXISTS `mess`;
CREATE TABLE `mess`  (
  `number` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `height` double NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mess
-- ----------------------------
INSERT INTO `mess` VALUES (1, '鸡翅', 0.1, 18, '鸡翅');
INSERT INTO `mess` VALUES (2, '鸡翅之妻', 2.1, 18, '侧颜杀');
INSERT INTO `mess` VALUES (3, '洪秀全之最严厉的父晋文帝', 2.8, 9999999, '神');
INSERT INTO `mess` VALUES (4, '春团', 2.5, 520, '兔子');
INSERT INTO `mess` VALUES (5, '了不起的小斗斗', 2, 12, '了不起');
INSERT INTO `mess` VALUES (6, '国学高手', 2.25, 5000, '高手');
INSERT INTO `mess` VALUES (7, '破晓圣战菲尼克斯', 2.151, 8888888, '不死鸟');
INSERT INTO `mess` VALUES (8, 'QQ捏捏好用到麦噗计算机', 0.75, 1, '计算机');
INSERT INTO `mess` VALUES (9, '一个宇宙超级霹雳旋风酷炫的名字', 10, 0, '名字');
INSERT INTO `mess` VALUES (10, '614视觉工作室', 3.1, 1, '房间');
INSERT INTO `mess` VALUES (11, '末日的仓鼠之王', 50, 7777777, '仓鼠');

SET FOREIGN_KEY_CHECKS = 1;
