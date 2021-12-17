package com.hack2win.dynamicrabbits.constant;

import com.hack2win.dynamicrabbits.dynamic.exception.ApiState;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO
 *
 * @author DongerKai
 * @since 2021/12/11 15:10 ，1.0
 **/

public class DynamicRabbitsConstant {
    @Getter
    @AllArgsConstructor
    public enum State implements ApiState {

        ERROR_START_END_TIME(4001, false, "结束时间必须大于开始时间！"),
        RECTANGULAR_REGION_RANGE_ERROR(4002, false, "矩形范围不正确！"),
        ERROR_FROM_SIZE_COUNT(4003, false, "偏移量加每页数量不能大于10000");

        private int code;
        private boolean status;
        private String message;
    }
}
