package com.donfyy.crowds.asm;

/**
 * Created by machao.mc on 2021/1/14
 * 用于自主开放 API 实现中直接 callback
 */
public interface LKEventInvokeCallback<T> {
    /**
     * callback 处理的结果
     *
     * @param result 结果
     */
    void callback(T result);
}
