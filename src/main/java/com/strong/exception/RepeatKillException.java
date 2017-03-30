package com.strong.exception;

/**
 * 重复秒杀异常（运行期异常）
 * Created by Ting on 17/3/24.
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
