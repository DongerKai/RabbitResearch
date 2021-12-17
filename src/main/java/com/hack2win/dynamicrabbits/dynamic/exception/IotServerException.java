package com.hack2win.dynamicrabbits.dynamic.exception;

import lombok.Getter;

/**
 * 系统服务异常
 *
 * @author DongerKai
 * @since 2019/1/14 13:30 ，1.0
 **/
public class IotServerException extends RuntimeException {

    @Getter
    private final ApiState state;

    private IotServerException(ApiState state, String message) {
        super(message);
        this.state = state;
    }

    private IotServerException(ApiState state) {
        this(state,state.getMessage());
    }

    private IotServerException(int code, boolean status, String message) {
        super(message);
        this.state = new ApiState() {
            @Override
            public int getCode() {
                return code;
            }

            @Override
            public boolean isStatus() {
                return status;
            }

            @Override
            public String getMessage() {
                return message;
            }
        };
    }

    public static IotServerException create(int code, boolean status, String message){
        return new IotServerException(code,status,message);
    }

    public static IotServerException create(ApiState state){
        return new IotServerException(state);
    }

    public static IotServerException create(ApiState state, String message){
        if(message == null)
            return new IotServerException(state);
       return new IotServerException(state,message);
    }
}
