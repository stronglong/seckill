package com.strong.dao;

import com.strong.model.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Ting on 17/3/22.
 */
public interface SuccesskilledDao {
    /**
     * 插入购买明细，可过滤重复
     */
    public Integer insertSuccessKilled(@Param("seckillId") Long seckillId, @Param("userPhone") String userPhone);

    /**
     * 根据seckillid查询success killed 并携带秒杀产品对象实体
     */
    public SuccessKilled queryByIdWithSeckill(@Param("seckillId") Long seckillId,@Param("userPhone") String userPhone);

}
