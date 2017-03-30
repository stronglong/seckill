package com.strong.common;

/**
 * 所有ajaz请求你返回类型，封装json结果
 * Created by Ting on 17/3/25.
 */
public class SeckillResult<T> {

    private boolean success;

    private T data;

    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }
}
