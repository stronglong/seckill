package com.strong.exception;

/**
 * 秒杀关闭异常
 * Created by Ting on 17/3/24.
 */
public class SeckillCloseException extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
