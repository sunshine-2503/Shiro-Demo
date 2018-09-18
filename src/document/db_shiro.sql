/*
SQLyog Ultimate v12.3.2 (64 bit)
MySQL - 5.7.17 : Database - db_shiro
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_shiro` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `db_shiro`;

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `menu_name` char(50) NOT NULL COMMENT '菜单名称',
  `menu_code` char(50) DEFAULT NULL COMMENT '菜单CODE',
  `menu_icon` char(100) DEFAULT NULL COMMENT '菜单图标',
  `menu_level` int(11) NOT NULL COMMENT '菜单层级',
  `order` int(11) NOT NULL DEFAULT '1' COMMENT '排序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tb_status` char(50) NOT NULL DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='菜单Menu表';

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`id`,`parent_id`,`menu_name`,`menu_code`,`menu_icon`,`menu_level`,`order`,`create_time`,`update_time`,`tb_status`) values 
(1,0,'权限管理','power',NULL,1,1,'2018-04-20 12:09:14','2018-04-20 12:09:14','正常'),
(2,1,'角色管理','power_role',NULL,2,1,'2018-04-20 13:36:48','2018-04-20 13:36:48','正常'),
(3,1,'菜单管理','power_menu',NULL,2,2,'2018-04-23 17:20:10','2018-04-23 17:22:04','正常'),
(4,1,'用户管理','power_user',NULL,2,3,'2018-04-23 17:21:12','2018-04-23 17:21:46','正常');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_name` char(50) NOT NULL COMMENT '角色名称',
  `desc` char(255) DEFAULT NULL COMMENT '描述',
  `order` int(11) NOT NULL DEFAULT '1' COMMENT '排序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tb_status` char(50) NOT NULL DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`role_name`,`desc`,`order`,`create_time`,`update_time`,`tb_status`) values 
(1,'超级管理员',NULL,1,'2018-04-23 17:16:25','2018-04-23 17:16:25','正常');

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tb_status` char(50) NOT NULL DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单关系表';

/*Data for the table `sys_role_menu` */

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_name` char(100) NOT NULL COMMENT '用户名',
  `password` char(100) DEFAULT NULL COMMENT '密码',
  `email` char(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` char(12) DEFAULT NULL COMMENT '手机号',
  `head_icon` char(100) DEFAULT NULL COMMENT '头像',
  `salt` char(50) DEFAULT NULL COMMENT '盐',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tb_status` char(50) NOT NULL DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统用户';

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`user_name`,`password`,`email`,`mobile`,`head_icon`,`salt`,`create_time`,`update_time`,`tb_status`) values 
(1,'admin','7733e7a488607211431c360bf843ed1b','2696212633@qq.com','17779705620',NULL,'F54GSD','2018-04-21 13:51:40','2018-04-23 16:57:54','正常');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tb_status` char(50) NOT NULL DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户与角色关系表';

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`id`,`user_id`,`role_id`,`create_time`,`update_time`,`tb_status`) values 
(1,1,1,'2018-04-23 17:17:35','2018-04-23 17:17:35','正常');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
