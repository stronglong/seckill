package com.strong.service.impl;

import com.strong.dao.SeckillDao;
import com.strong.dao.SuccesskilledDao;
import com.strong.dao.cache.RedisDao;
import com.strong.dto.Exposer;
import com.strong.dto.SeckillExecution;
import com.strong.enums.SeckillStateEnum;
import com.strong.exception.RepeatKillException;
import com.strong.exception.SeckillCloseException;
import com.strong.exception.SeckillException;
import com.strong.model.Seckill;
import com.strong.model.SuccessKilled;
import com.strong.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Ting on 17/3/24.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccesskilledDao successkilledDao;

    @Autowired
    private RedisDao redisDao;

    //盐值字符串，用于混淆md5
    private static final String slat = "kadjsfLADSFLDL^&*%&^*&;;.JH683274";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 10);
    }

    @Override
    public Seckill getById(Long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(Long seckilId) {
//        Seckill seckill = seckillDao.queryById(seckilId);
//        if (seckill == null) {
//            return new Exposer(false, seckilId);
//        }
        // get from redis
        Seckill seckill = redisDao.getSeckill(seckilId);
        if (seckill == null) {
            // from db
            seckill = seckillDao.queryById(seckilId);
            if (seckilId == null) {
                return new Exposer(false, seckilId);
            }
            // put redis
            redisDao.pullSeckill(seckill);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date now = new Date();
        if (now.getTime() < startTime.getTime()
                || now.getTime() > endTime.getTime()) {
            return new Exposer(false, seckilId, now.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMd5(seckilId);
        return new Exposer(true, md5, seckilId);
    }

//    @Override
//    @Transactional
//    /**
//     * 使用注解控制事务方法的优点：
//     * 1:开发团队达成一致约定，明确标注事务方法的编程分格
//     * 2: 保证事务执行的时间尽可能的短，不要穿插其他网络操作，或者剥离到事务方法外
//     * 3: 不是所有方法都需要事务，如只有一条修改操作,只读操作不需要事务
//     */
//    public SeckillExecution executeSeckill(Long seckillId, String userPhone, String md5)
//            throws SeckillException, RepeatKillException, SeckillCloseException {
//        if (md5 == null || !md5.equals(getMd5(seckillId))) {
//            throw new SeckillException("seckill data rewrite");
//        }
//        try {
//            //执行秒杀逻辑：减库存＋记录购买行为
//            int updateCount = seckillDao.reduceNumber(seckillId, new Date());
//            if (updateCount <= 0) {
//                //没有更新到记录,秒杀结束
//                throw new SeckillCloseException("seckill is closed");
//            } else {
//                // 记录购买行为
//                if (isNotExist(seckillId, userPhone)) {
//                    successkilledDao.insertSuccessKilled(seckillId, userPhone);
//                } else {
//                    throw new RepeatKillException("seckill repeated");
//                }
//                //秒杀成功
//                SuccessKilled successKilled = successkilledDao.queryByIdWithSeckill(seckillId, userPhone);
//                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
//            }
//        } catch (SeckillCloseException e1) {
//            throw e1;
//        } catch (RepeatKillException e2) {
//            throw e2;
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            //  所有编译期异常，转化为运行期异常
//            throw new SeckillException("seckill inner error:" + e.getMessage());
//        }
//    }


    @Override
    public SeckillExecution executeSeckill(Long seckillId, String userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存＋记录购买行为
        try {
            if (!isNotExist(seckillId, userPhone)) {
                throw new RepeatKillException("seckill repeated");
            } else {
                //记录购买行为
                int insertCount = successkilledDao.insertSuccessKilled(seckillId, md5);
                //唯一：seckillId,userPhone
                if (insertCount <= 0) {
                    //  重复秒杀
                    throw new RepeatKillException("seckill repeated");
                } else {
                    // 减库存，热点商品竞争
                    int updateCount = seckillDao.reduceNumber(seckillId, new Date());
                    if (updateCount <= 0) {
                        //没有更新到记录，秒杀结束
                        throw new SeckillCloseException("seckill is closed");
                    } else {
                        //秒杀成功
                        SuccessKilled successKilled = successkilledDao.queryByIdWithSeckill(seckillId, userPhone);
                        return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                    }
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常转化为运行时异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(Long seckillId, String userPhone, String md5) {
        Map<String, Object> map = new HashedMap();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", new Date());
        map.put("result", null);
        try {
            if (!isNotExist(seckillId, userPhone)) {
                return new SeckillExecution(seckillId,SeckillStateEnum.REPEAT_KILL);
            } else {
                seckillDao.killByProcedure(map);
                int result = MapUtils.getInteger(map, "result", -2);
                if (result == 1) {
                    SuccessKilled sk = successkilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, sk);
                }
                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }

    private String getMd5(Long seckillId) {
        String base = seckillId + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    private boolean isNotExist(Long seckillId, String userPhone) {
        SuccessKilled successKilled = successkilledDao.queryByIdWithSeckill(seckillId, userPhone);
        if (successKilled == null) {
            return true;
        }
        return false;
    }
}
