/*
Navicat MySQL Data Transfer

Source Server         : otess-123.57.3.145
Source Server Version : 50522
Source Host           : 123.57.3.145:3306
Source Database       : otess

Target Server Type    : MYSQL
Target Server Version : 50522
File Encoding         : 65001

Date: 2016-08-28 15:03:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mp_action
-- ----------------------------
DROP TABLE IF EXISTS `mp_action`;
CREATE TABLE `mp_action` (
  `a_id` int(11) NOT NULL AUTO_INCREMENT,
  `a_code` int(11) DEFAULT NULL,
  `a_path` varchar(255) DEFAULT NULL,
  `a_title` varchar(255) DEFAULT NULL,
  `a_pcode` int(11) DEFAULT '0',
  `a_order` int(11) DEFAULT NULL,
  `a_status` int(11) DEFAULT '1',
  `a_icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`a_id`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mp_action
-- ----------------------------
INSERT INTO `mp_action` VALUES ('1', '1000', ' ', '系统管理', '0', '8', '1', ' ');
INSERT INTO `mp_action` VALUES ('2', '1100', 'user/list', '用户管理', '1000', '1', '1', ' ');
INSERT INTO `mp_action` VALUES ('3', '1200', 'role/list', '角色管理', '1000', '2', '1', ' ');
INSERT INTO `mp_action` VALUES ('4', '1300', 'fold/list', '分组管理', '1000', '3', '1', ' ');
INSERT INTO `mp_action` VALUES ('5', '2000', ' ', '终端管理', '0', '0', '1', ' ');
INSERT INTO `mp_action` VALUES ('6', '2100', 'client/list', '终端列表', '2000', '1', '1', ' ');
INSERT INTO `mp_action` VALUES ('7', '2200', 'logsend/list', '发送队列', '2000', '2', '1', ' ');
INSERT INTO `mp_action` VALUES ('8', '3000', ' ', '任务管理', '0', '3', '1', ' ');
INSERT INTO `mp_action` VALUES ('9', '3100', 'task/list', '任务列表', '3000', '4', '1', ' ');
INSERT INTO `mp_action` VALUES ('10', '4000', ' ', '资源管理', '0', '6', '1', ' ');
INSERT INTO `mp_action` VALUES ('11', '4100', 'mediafile/list', '资源列表', '4000', '3', '1', ' ');
INSERT INTO `mp_action` VALUES ('12', '4200', 'filetype/list', '文件类型', '4000', '4', '1', ' ');
INSERT INTO `mp_action` VALUES ('13', '4300', ' ', '转码任务', '4000', '0', '0', ' ');
INSERT INTO `mp_action` VALUES ('14', '1400', 'sysconfig', '基本设定', '1000', '0', '1', ' ');
INSERT INTO `mp_action` VALUES ('15', '3200', 'screen/list', '屏幕布局', '3000', '5', '1', null);
INSERT INTO `mp_action` VALUES ('16', '5000', ' ', '日志管理', '0', '7', '1', null);
INSERT INTO `mp_action` VALUES ('17', '5100', 'syslog/list', '日志操作', '5000', '1', '1', null);
INSERT INTO `mp_action` VALUES ('18', '1500', 'setting/upgrade', '升级配置', '1000', '0', '1', ' ');

-- ----------------------------
-- Table structure for mp_client
-- ----------------------------
DROP TABLE IF EXISTS `mp_client`;
CREATE TABLE `mp_client` (
  `cl_id` int(11) NOT NULL AUTO_INCREMENT,
  `cl_name` varchar(50) CHARACTER SET utf8 DEFAULT '',
  `cl_deviceid` varchar(50) CHARACTER SET utf8 NOT NULL,
  `cl_ip` varchar(50) CHARACTER SET utf8 DEFAULT '',
  `cl_ip_long` bigint(20) DEFAULT NULL,
  `cl_mac` varchar(50) CHARACTER SET utf8 DEFAULT '',
  `cl_status` int(11) DEFAULT '0',
  `cl_last_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `cl_send_task_id` int(11) DEFAULT '0',
  `cl_start_time` int(11) DEFAULT '0',
  `cl_is_del` int(11) DEFAULT '0',
  `cl_shut_time` int(11) DEFAULT '0',
  `cl_fold_id` int(11) DEFAULT '0',
  `cl_version` varchar(50) CHARACTER SET utf8 DEFAULT '',
  `cl_build_number` int(11) DEFAULT '0',
  PRIMARY KEY (`cl_id`)
) ENGINE=MyISAM AUTO_INCREMENT=296 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mp_client_monitor_img
-- ----------------------------
DROP TABLE IF EXISTS `mp_client_monitor_img`;
CREATE TABLE `mp_client_monitor_img` (
  `i_id` int(11) NOT NULL AUTO_INCREMENT,
  `i_deviceid` varchar(50) DEFAULT NULL,
  `i_img` mediumtext,
  `createdat` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`i_id`)
) ENGINE=MyISAM AUTO_INCREMENT=604 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mp_client_status
-- ----------------------------
DROP TABLE IF EXISTS `mp_client_status`;
CREATE TABLE `mp_client_status` (
  `deviceid` varchar(50) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `createdat` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`deviceid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mp_filetype
-- ----------------------------
DROP TABLE IF EXISTS `mp_filetype`;
CREATE TABLE `mp_filetype` (
  `f_id` int(11) NOT NULL AUTO_INCREMENT,
  `f_name` varchar(50) NOT NULL,
  `f_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`f_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mp_filetype
-- ----------------------------
INSERT INTO `mp_filetype` VALUES ('1', '文本', 'txt,log');
INSERT INTO `mp_filetype` VALUES ('2', '网页', 'html,htm,mht');
INSERT INTO `mp_filetype` VALUES ('3', '图片', 'jpg,gif,png,bmp,jpeg');
INSERT INTO `mp_filetype` VALUES ('4', '音频', 'mid,wav,mp3,wma,swf');
INSERT INTO `mp_filetype` VALUES ('5', '视频', 'flv,wmv,avi,mp4,mpg');
INSERT INTO `mp_filetype` VALUES ('6', 'PDF', 'pdf');
INSERT INTO `mp_filetype` VALUES ('7', '直播', 'http,rtmp,rtsp');

-- ----------------------------
-- Table structure for mp_fold
-- ----------------------------
DROP TABLE IF EXISTS `mp_fold`;
CREATE TABLE `mp_fold` (
  `cf_id` int(11) NOT NULL AUTO_INCREMENT,
  `cf_name` varchar(32) DEFAULT NULL,
  `cf_info` varchar(50) DEFAULT NULL,
  `cf_pid` int(11) NOT NULL,
  PRIMARY KEY (`cf_id`)
) ENGINE=MyISAM AUTO_INCREMENT=88 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mp_log_send
-- ----------------------------
DROP TABLE IF EXISTS `mp_log_send`;
CREATE TABLE `mp_log_send` (
  `ls_id` int(11) NOT NULL AUTO_INCREMENT,
  `ls_task_id` int(11) DEFAULT '0',
  `ls_client_id` int(11) NOT NULL DEFAULT '0',
  `ls_type` int(11) NOT NULL DEFAULT '0',
  `ls_time_send` datetime DEFAULT '0000-00-00 00:00:00',
  `ls_send_file_ids` text CHARACTER SET utf8,
  `ls_last_file_id` int(11) DEFAULT '0',
  `ls_last_present` int(11) DEFAULT '0',
  `ls_attribute` int(11) DEFAULT '0',
  `ls_last_second` int(11) DEFAULT '0',
  `ls_speed` varchar(50) DEFAULT '',
  `ls_total_percent` int(11) DEFAULT '0',
  `userid` int(50) DEFAULT '0',
  PRIMARY KEY (`ls_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3116 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mp_media_file
-- ----------------------------
DROP TABLE IF EXISTS `mp_media_file`;
CREATE TABLE `mp_media_file` (
  `m_id` int(11) NOT NULL AUTO_INCREMENT,
  `m_name` varchar(128) NOT NULL,
  `m_file` varchar(128) NOT NULL DEFAULT '',
  `m_size_low` int(11) DEFAULT '0',
  `m_size_high` int(11) DEFAULT '0',
  `m_type` int(11) DEFAULT '0',
  `m_add_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `m_customer_no` varchar(128) DEFAULT NULL,
  `m_no` varchar(128) DEFAULT NULL,
  `m_from_top` int(11) DEFAULT '0',
  `m_is_del` int(11) DEFAULT '0',
  `m_upsize_high` int(11) DEFAULT '0',
  `m_upsize_low` int(11) DEFAULT '0',
  `m_duration` int(11) DEFAULT '0',
  `m_price` decimal(18,2) DEFAULT '0.00',
  `m_p2p_hash` varchar(500) DEFAULT NULL,
  `m_userid` int(11) DEFAULT '0',
  `m_state` int(11) DEFAULT '0',
  PRIMARY KEY (`m_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1658 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mp_operator
-- ----------------------------
DROP TABLE IF EXISTS `mp_operator`;
CREATE TABLE `mp_operator` (
  `o_id` int(11) NOT NULL AUTO_INCREMENT,
  `o_type` int(11) DEFAULT NULL,
  `o_deviceid` text,
  `o_param` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`o_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1933 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mp_operator
-- ----------------------------

-- ----------------------------
-- Table structure for mp_resolution
-- ----------------------------
DROP TABLE IF EXISTS `mp_resolution`;
CREATE TABLE `mp_resolution` (
  `mr_id` int(11) NOT NULL AUTO_INCREMENT,
  `mr_w` int(11) DEFAULT '0',
  `mr_h` int(11) DEFAULT '0',
  `istype` int(11) DEFAULT '0',
  PRIMARY KEY (`mr_id`)
) ENGINE=MyISAM AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mp_role
-- ----------------------------
DROP TABLE IF EXISTS `mp_role`;
CREATE TABLE `mp_role` (
  `r_id` int(11) NOT NULL AUTO_INCREMENT,
  `r_name` varchar(50) DEFAULT NULL,
  `r_permission` varchar(1000) DEFAULT NULL,
  `r_type` int(11) DEFAULT '0' COMMENT '0=普通，1=审核，2=超级',
  `r_desc` varchar(255) DEFAULT NULL,
  `r_status` int(1) DEFAULT NULL,
  PRIMARY KEY (`r_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mp_role
-- ----------------------------
INSERT INTO `mp_role` VALUES ('1', '超级管理', '2000,2100,2200,3000,3100,3200,4000,4100,4200,5000,5100,1000,1400,1500,1100,1200,1300', '2', '具有所有权限', '1');
INSERT INTO `mp_role` VALUES ('2', '审核员', '4000,4200,4300,4100,2000,2100,2200,3000,3100,3200', '1', '审核任务等', '1');
INSERT INTO `mp_role` VALUES ('3', '普通用户', '2000,2100,2200,3000,3100,4000,4100,4200,5000,5100', '0', '普通权限', '1');

-- ----------------------------
-- Table structure for mp_screen
-- ----------------------------
DROP TABLE IF EXISTS `mp_screen`;
CREATE TABLE `mp_screen` (
  `sc_id` int(11) NOT NULL AUTO_INCREMENT,
  `sc_name` varchar(255) DEFAULT NULL,
  `sc_target` varchar(1000) DEFAULT '',
  `sc_info` varchar(1000) DEFAULT NULL,
  `sc_default` int(11) DEFAULT '0',
  `sc_rid` int(11) DEFAULT '0',
  PRIMARY KEY (`sc_id`)
) ENGINE=MyISAM AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mp_screen
-- ----------------------------

-- ----------------------------
-- Table structure for mp_setting
-- ----------------------------
DROP TABLE IF EXISTS `mp_setting`;
CREATE TABLE `mp_setting` (
  `s_key` varchar(50) NOT NULL,
  `s_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`s_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mp_setting
-- ----------------------------
INSERT INTO `mp_setting` VALUES ('k_log', 'true');
INSERT INTO `mp_setting` VALUES ('k_upgrade', '{\"downloadurl\":\"http:\\/\\/123.57.3.145:8080\\/otess\\/app-release2016.6.7.apk\",\"force\":\"true\",\"version\":\"4\",\"desc\":\"aa\"}');
INSERT INTO `mp_setting` VALUES ('k_version', 'V5.8');

-- ----------------------------
-- Table structure for mp_syslog
-- ----------------------------
DROP TABLE IF EXISTS `mp_syslog`;
CREATE TABLE `mp_syslog` (
  `sl_id` int(11) NOT NULL AUTO_INCREMENT,
  `sl_type` int(11) DEFAULT '0',
  `sl_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `sl_log` varchar(50) DEFAULT NULL,
  `sl_mac` varchar(50) DEFAULT NULL,
  `sl_ip` varchar(50) DEFAULT NULL,
  `sl_userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sl_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4775 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mp_syslogtype
-- ----------------------------
DROP TABLE IF EXISTS `mp_syslogtype`;
CREATE TABLE `mp_syslogtype` (
  `lt_id` int(11) NOT NULL DEFAULT '0',
  `lt_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`lt_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mp_syslogtype
-- ----------------------------
INSERT INTO `mp_syslogtype` VALUES ('1', '系统操作');
INSERT INTO `mp_syslogtype` VALUES ('2', '系统错误');
INSERT INTO `mp_syslogtype` VALUES ('3', '发送日志');
INSERT INTO `mp_syslogtype` VALUES ('4', '接收日志');
INSERT INTO `mp_syslogtype` VALUES ('1000', '登录日志');
INSERT INTO `mp_syslogtype` VALUES ('1001', '用户相关');
INSERT INTO `mp_syslogtype` VALUES ('1002', '任务相关');
INSERT INTO `mp_syslogtype` VALUES ('1003', '资源相关');
INSERT INTO `mp_syslogtype` VALUES ('1004', '终端管理');

-- ----------------------------
-- Table structure for mp_task
-- ----------------------------
DROP TABLE IF EXISTS `mp_task`;
CREATE TABLE `mp_task` (
  `t_id` int(11) NOT NULL AUTO_INCREMENT,
  `t_name` varchar(50) CHARACTER SET utf8 NOT NULL,
  `t_type` int(11) NOT NULL,
  `t_loop` int(11) NOT NULL,
  `t_no` varchar(50) CHARACTER SET utf8 NOT NULL,
  `t_add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `t_begin_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `t_end_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `t_weeks` varchar(50) CHARACTER SET utf8 DEFAULT '',
  `t_status` int(11) NOT NULL,
  `mr_id` int(11) NOT NULL,
  `t_resolution` int(11) NOT NULL,
  `t_userid` int(11) NOT NULL DEFAULT '-1',
  `t_simple` int(11) DEFAULT '0' COMMENT '0=否 1=是',
  `o_status` int(11) NOT NULL DEFAULT '-1',
  `o_mark` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `o_userid` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`t_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1345 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mp_task_file
-- ----------------------------
DROP TABLE IF EXISTS `mp_task_file`;
CREATE TABLE `mp_task_file` (
  `tf_id` int(11) NOT NULL AUTO_INCREMENT,
  `tf_task_item_id` int(11) NOT NULL DEFAULT '0',
  `tf_position` int(11) NOT NULL DEFAULT '0',
  `tf_index` int(11) NOT NULL DEFAULT '0',
  `tf_type` int(11) NOT NULL DEFAULT '0',
  `tf_media_no` varchar(128) CHARACTER SET utf8 DEFAULT '',
  `tf_duration` int(11) NOT NULL DEFAULT '0',
  `tf_style` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `tf_style_web` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `tf_bgsound` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `tf_http_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `tf_affdate_weather` varchar(1000) CHARACTER SET utf8 DEFAULT NULL,
  `tf_affix_url` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`tf_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2772 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mp_task_item
-- ----------------------------
DROP TABLE IF EXISTS `mp_task_item`;
CREATE TABLE `mp_task_item` (
  `ti_id` int(11) NOT NULL AUTO_INCREMENT,
  `ti_task_id` int(11) NOT NULL DEFAULT '0',
  `ti_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `ti_duration` int(11) NOT NULL DEFAULT '0',
  `ti_screen_type` varchar(1500) CHARACTER SET utf8 NOT NULL,
  `ti_index` int(11) NOT NULL DEFAULT '0',
  `ti_screen_type_affix` varchar(1500) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`ti_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1441 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mp_user
-- ----------------------------
DROP TABLE IF EXISTS `mp_user`;
CREATE TABLE `mp_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(16) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `groupid` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `pid` int(11) DEFAULT '0',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mp_user
-- ----------------------------
INSERT INTO `mp_user` VALUES ('1', 'admin', ' ', 'admin', '1', '0', '1', '0', '2016-02-23 15:50:02');
INSERT INTO `mp_user` VALUES ('32', '11', '11', '11', '3', '84', '1', '31', '2016-08-09 11:52:47');
INSERT INTO `mp_user` VALUES ('31', '1', '1', '1', '2', '0', '1', '0', '2016-06-30 14:18:13');
INSERT INTO `mp_user` VALUES ('26', 'audit', 'audit', 'audit', '2', '0', '1', '0', '2016-06-15 16:14:51');

