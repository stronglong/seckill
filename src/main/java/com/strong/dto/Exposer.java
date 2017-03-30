package com.strong.dto;

import lombok.Data;

/**
 * 暴露秒杀地址dto
 * Created by Ting on 17/3/24.
 */
@Data
public class Exposer {

    //是否开启秒杀
    private boolean exposed;

    //一种加密措施
    private String md5;

    //秒杀ID
    private Long seckillId;

    //系统当前时间
    private Long now;

    //秒杀开始时间
    private Long start;

    //秒杀结束时间
    private Long end;

    public Exposer(boolean exposed, String md5, Long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, Long seckillId, Long now, Long start, Long end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposed, Long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }
}
