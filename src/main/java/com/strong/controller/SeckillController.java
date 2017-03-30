package com.strong.controller;

import com.strong.common.Response;
import com.strong.dto.Exposer;
import com.strong.dto.SeckillExecution;
import com.strong.enums.SeckillStateEnum;
import com.strong.exception.RepeatKillException;
import com.strong.exception.SeckillCloseException;
import com.strong.model.Seckill;
import com.strong.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * Created by Ting on 17/3/24.
 */
@Controller
@RequestMapping("/seckill")//url:/模块/资源/{id}/细分   /seckill/list
public class SeckillController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    /**
     * 秒杀列表页
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        //list.jsp + model = ModelAndView
        return "list";
    }

    /**
     * 秒杀详情页
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 获取秒杀地址
     */
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            return Response.ok(exposer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.fail(e.getMessage());
        }
    }

    /**
     * 执行秒杀
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execute",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                              @PathVariable("md5") String md5,
                                              @CookieValue(value = "killPhone", required = false) String phone) {
        if (phone == null) {
            return Response.fail("未注册");
        }
        SeckillExecution execution = null;
        try {
//            execution = seckillService.executeSeckill(seckillId, phone, md5);
            // 存储过程
            execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            return Response.ok(execution);
        } catch (RepeatKillException e) {
            execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new Response<>(true, execution);
        } catch (SeckillCloseException e) {
            execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new Response<>(true, execution);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new Response<>(true, execution);
        }
    }

    /**
     * 获取系统时间
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public Response<Long> getTime() {
        Date now = new Date();
        return Response.ok(now.getTime());
    }
}
