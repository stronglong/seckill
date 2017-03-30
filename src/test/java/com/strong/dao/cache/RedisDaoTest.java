package com.strong.dao.cache;

import com.strong.dao.SeckillDao;
import com.strong.model.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Ting on 17/3/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    private long id = 1001;

    @Test
    public void testSeckill() throws Exception {
        // get & put
        Seckill seckill = redisDao.getSeckill(id);
        if (seckill == null) {
            seckill = seckillDao.queryById(id);
            if (seckill != null) {
                String result = redisDao.pullSeckill(seckill);
                System.out.println("result:" + result);
                seckill = redisDao.getSeckill(id);
                System.out.println("seckill:" + seckill);
            }
        }
    }

}