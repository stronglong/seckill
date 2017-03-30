package com.strong.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ting on 17/3/22.
 */
@Data
public class Seckill {

    private Long seckillId;
    private String name;
    private Integer number;
    private Date startTime;
    private Date endTime;
    private Date createTime;
}
