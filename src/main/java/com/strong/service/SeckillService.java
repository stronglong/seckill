package com.strong.service;

import com.strong.dto.Exposer;
import com.strong.dto.SeckillExecution;
import com.strong.exception.RepeatKillException;
import com.strong.exception.SeckillCloseException;
import com.strong.exception.SeckillException;
import com.strong.model.Seckill;

import java.util.List;

/**
 * 业务接口：站在使用者角度设计接口
 * 三个方面：方法定义的粒度，参数，返回类型（return 类型／异常）
 * Created by Ting on 17/3/24.
 */
public interface SeckillService {
    /**
     * 查找所有秒杀
     */
    List<Seckill> getSeckillList();

    /**
     * 查找单个秒杀
     */
    Seckill getById(Long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址，
     * 否则输出系统时间和秒杀时间
     */
    Exposer exportSeckillUrl(Long seckilId);


    /**
     * 执行秒杀操作
     */
    SeckillExecution executeSeckill(Long seckillId, String userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 执行秒杀 by 存储过程
     */
    SeckillExecution executeSeckillProcedure(Long seckillId,String userPhone,String md5);



}
