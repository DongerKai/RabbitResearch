package com.hack2win.dynamicrabbits.dynamic.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 策略配置
 */
@Data
@Setter
@Getter
public class Strategy {
    private Integer id;
    /**
     * 规则k
     */
    private String topic;
    /**
     * 规则名称
     */
    private String routingName;
    /**
     * 规则信息
     */
    private String bindingQueue;

    /**
     * 规则描述信息
     */
    private String description;
    /**
     * 是否在redis缓存里
     */
    private String isCache;
    /**
     * 是否启用
     */
    private String isEnable;
    /**
     * 逻辑删除
     */
    private String isDelete;

    private int remainRetryTimes;

}
