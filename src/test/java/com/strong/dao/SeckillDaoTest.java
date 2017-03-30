package com.strong.dao;

import com.strong.model.Seckill;
import com.sun.tools.corba.se.idl.InterfaceGen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Ting on 17/3/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDaoTest {

    @Resource
    private SeckillDao seckillDao;


    @Test
    public void testQueryById() throws Exception {
        Long id = 1000L;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        Integer updateCount = seckillDao.reduceNumber(1000l, killTime);
        System.out.println("updateCount=" + updateCount);


    }


    @Test
    public void queryAll() throws Exception {
        List<Seckill> list = seckillDao.queryAll(1, 5);
        for (Seckill seckill : list) {
            System.out.println(seckill);
        }
    }

}