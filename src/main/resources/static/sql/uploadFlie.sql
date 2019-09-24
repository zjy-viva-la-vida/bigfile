DROP TABLE IF EXISTS  `upload_file`;

CREATE TABLE `upload_file` (
  `id` varchar(64) NOT NULL,
  `path` varchar(1024) NOT NULL COMMENT '文件存储路径',
  `size` varchar(64) NOT NULL COMMENT '文件大小',
  `suffix` varchar(16) NOT NULL COMMENT '文件后缀',
  `total_block` int(11) NOT NULL COMMENT '文件一共分成了多少块',
  `file_index` int(11) NOT NULL COMMENT '文件上传到第几个块了，从1开始',
  `name` varchar(64) NOT NULL COMMENT '文件名',
  `file_md5` varchar(64) NOT NULL COMMENT '文件md5值',
  `create_time` timestamp NOT NULL DEFAULT '2019-09-21 10:30:40',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL COMMENT '文件状态:1文件未完全上传；2文件已完全上传',
  `deleted` int(11) NOT NULL COMMENT '删除状态：1 已删除 0 未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件存储表';

SET FOREIGN_KEY_CHECKS = 1;