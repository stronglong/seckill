package com.strong.dto;

import com.strong.enums.SeckillStateEnum;
import com.strong.model.SuccessKilled;
import lombok.Data;

/**
 * 秒杀执行后的结果
 * Created by Ting on 17/3/24.
 */
@Data
public class SeckillExecution {

    //秒杀ID
    private Long seckillId;

    //秒杀执行结果状态
    private Integer state;

    //状态表示
    private String stateInfo;

    //秒杀成功对象
    private SuccessKilled successKilled;

    public SeckillExecution(Long seckillId, SeckillStateEnum stateEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(Long seckillId, SeckillStateEnum stateEnum) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }
}
