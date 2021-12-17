package com.hack2win.dynamicrabbits.dynamic.utils;

import com.hack2win.dynamicrabbits.constant.DynamicRabbitsConstant;
import com.hack2win.dynamicrabbits.dynamic.exception.ApiState;
import com.hack2win.dynamicrabbits.dynamic.exception.IotServerException;
import lombok.extern.slf4j.Slf4j;


/**
 * 方法执行工具类
 *
 * @author DongerKai
 * @since 2019/1/14 13:30 ，1.0
 **/
@Slf4j
public class FunctionUtils {

    /**
     * 执行但是不关心成功没有，忽略异常
     *
     * 使用方法：
     * <code>FunctionUtil.applyIgnoreException(() -> System.out.print("hi"))</code>
     */
    public static <T> T applyIgnoreException(TAction<T> action) {
        try {
            return action.apply();
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 执行但是不关心成功没有，忽略异常
     */
    public static void applyIgnoreException(VoidAction action) {
        try {
            action.apply();
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    }

    /**
     * 执行关心是否成功，但是抑制抛出的异常 ，修改为系统异常
     */
    public static <T> T applyThrowException(TAction<T> action) {
        try {
            return action.apply();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw IotServerException.create(DynamicRabbitsConstant.State.ERROR_FROM_SIZE_COUNT);
        }
    }

    /**
     * 执行关心是否成功，但是抑制抛出的异常 ，修改为系统异常
     */
    public static void applyThrowException(VoidAction action) {
        try {
            action.apply();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw IotServerException.create(DynamicRabbitsConstant.State.ERROR_FROM_SIZE_COUNT);
        }
    }

    /**
     * 执行关心是否成功，但是抑制抛出的异常 ，修改为自定义异常
     */
    public static <T> T applyThrowException(TAction<T> action,RuntimeException exception) {
        try {
            return action.apply();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw exception;
        }
    }

    /**
     * 执行关心是否成功，但是抑制抛出的异常 ，修改为自定义异常
     */
    public static void applyThrowException(VoidAction action,RuntimeException exception) {
        try {
            action.apply();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw exception;
        }
    }

    public interface TAction<T> {
        T apply() throws Exception;
    }

    public interface VoidAction {
        void apply() throws Exception;
    }
}
