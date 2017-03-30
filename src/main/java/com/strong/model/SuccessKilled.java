package com.strong.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ting on 17/3/22.
 */
@Data
public class SuccessKilled {

    private Long id;
    private Long seckillId;
    private String userPhone;
    private Integer state;
    private Date createTime;
    private Seckill seckill;
}
