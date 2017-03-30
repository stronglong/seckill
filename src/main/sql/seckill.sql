-- 秒杀之行存储过程
-- 定义存储过程
-- 参数： in 输入参数 out输出参数
--  row_count(): 返回上一条修改类型sql（insert，delete，update）的影响行数
--  row_count(): 未修改数据； 0 > sql: 表示修改的行数；0 < sql: 错误／未执行修改sql

DELIMITER $$ -- console; 转换为$$
CREATE PROCEDURE `seckill`.`execute_seckill`(in v_seckill_id bigint,in v_phone bigint,
  in v_kill_time timestamp,out r_result int)
  BEGIN
    DECLARE insert_count int DEFAULT 0;
    START TRANSACTION ;
    insert into success_killed
      (seckill_id,user_phone,create_time)
      values (v_seckill_id,v_phone,v_kill_time);
    SELECT row_count() into insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK ;
      set r_result = -1;
    ELSEIF (insert_count < 0) THEN
      ROLLBACK ;
      set r_result = -2;
    ELSE
      update seckill
      set number = number -1
      where seckill_id = v_seckill_id
        and end_time > v_kill_time
        and start_time < v_kill_time
        and number > 0;
      select row_count() into insert_count;
      IF (insert_count = 0) THEN
        ROLLBACK ;
        set r_result = 0;
      ELSEIF (insert_count < 0) THEN
        ROLLBACK ;
        set r_result = -2;
      ELSE
        COMMIT ;
        set r_result = 1;
      END IF;
    END IF;
  END ;
$$
	－－存储过程定义结束
DELIMITER ;
set @r_result = -3;
-- execute
call execute_seckill(1003,18380413677,0,now(),@r_result);
-- get result
select @r_result;

-- 存储过程
--  1: 存储过程优化：事务行级锁持有的时间
--  2: 不要过度依赖存储过程
--  3： 简单的逻辑可以应用存储过程

