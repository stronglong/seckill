package com.strong.dao;

import com.strong.model.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Ting on 17/3/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccesskilledDaoTest {


    @Resource
    private SuccesskilledDao successkilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        int count = successkilledDao.insertSuccessKilled(1001L,"18181428355");
        System.out.println("count="+count);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        SuccessKilled successKilled = successkilledDao.queryByIdWithSeckill(1001L, "18181428355");
        System.out.println(successKilled);
    }

}