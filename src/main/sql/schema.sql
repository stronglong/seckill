-- 数据库初始化脚本

－－ 创建数据库
CREATE database seckill;

－－创建秒杀库存表
CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品存库ID',
  `name` varchar(126) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` datetime NOT NULL COMMENT '秒杀开始时间',
  `end_time` datetime NOT NULL COMMENT '秒杀结束时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

--初始化数据
INSERT INTO seckill(name,number,start_time,end_time)
VALUES
('1000元秒杀iphone6s',100,'2017-03-01 00:00:00','2017-03-15 00:00:00'),
('500元秒杀ipad2',200,'2017-03-01 00:00:00','2017-03-15 00:00:00'),
('300元秒杀小米4s',300,'2017-03-01 00:00:00','2017-03-15 00:00:00'),
('200元秒杀红米note',400,'2017-03-01 00:00:00','2017-03-15 00:00:00'),
('100元秒杀乐视1s',500,'2017-03-01 00:00:00','2017-03-15 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关的信息
CREATE TABLE `success_killed` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品ID',
  `user_phone` varchar(64) NOT NULL DEFAULT '' COMMENT '用户手机',
  `state` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '状态标识:-1无效 0成功 1已付款,2已发货',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_phone` (`user_phone`),
  KEY `idx_seckill_id` (`seckill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';