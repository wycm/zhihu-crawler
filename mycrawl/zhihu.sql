

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for href
-- ----------------------------
DROP TABLE IF EXISTS `href`;
CREATE TABLE `href` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `href` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `href` (`href`(255))
) ENGINE=InnoDB AUTO_INCREMENT=2227817 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(255) DEFAULT NULL,
  `business` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `employment` varchar(255) DEFAULT NULL,
  `education` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `agrees` int(11) DEFAULT NULL,
  `thanks` int(11) DEFAULT NULL,
  `asks` int(11) DEFAULT NULL,
  `answers` int(11) DEFAULT NULL,
  `posts` int(11) DEFAULT NULL,
  `followees` int(11) DEFAULT NULL,
  `followers` int(11) DEFAULT NULL,
  `hashId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=976838 DEFAULT CHARSET=utf8;
