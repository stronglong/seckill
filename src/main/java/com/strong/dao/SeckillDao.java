package com.strong.dao;

import com.strong.model.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Ting on 17/3/22.
 */
public interface SeckillDao {

    /**
     * 减库存
     */
    public Integer reduceNumber(@Param("seckillId") Long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据ID查询秒杀对象
     */
    public Seckill queryById(Long seckillId);

    /**
     * 根据偏移量查询秒杀列表
     */
    public List<Seckill> queryAll(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 使用存储过程执行秒杀
     */
    public void killByProcedure(Map<String, Object> map);


}
