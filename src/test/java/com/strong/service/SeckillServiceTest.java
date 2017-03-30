package com.strong.service;

import com.strong.dto.Exposer;
import com.strong.dto.SeckillExecution;
import com.strong.exception.RepeatKillException;
import com.strong.exception.SeckillException;
import com.strong.model.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Ting on 17/3/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void getById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1001l;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            String phone = "18181428355";
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillException e) {
                logger.warn(e.getMessage());
            }
        } else {
            logger.warn("exposer={}", exposer);
        }

    }

    @Test
    public void executeSeckillProcdure() {
        long id = 1001;
        String phone   = "18709871234";
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(id,phone,md5);
            logger.info(execution.getStateInfo());
        }

    }


}