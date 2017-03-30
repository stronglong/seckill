package com.strong.exception;

/**
 * 秒杀相关业务异常
 * Created by Ting on 17/3/24.
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
