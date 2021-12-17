package com.hack2win.dynamicrabbits.dynamic.exception;


/**
 * 返回错误码 接口
 *
 * @author DongerKai
 * @since 2019/1/14 21:09 ，1.0
 **/
public interface ApiState {

    int getCode();

    boolean isStatus();

    String getMessage();
}
