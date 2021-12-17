package com.hack2win.dynamicrabbits.dynamic.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息记录实体
 */
@Data
@Setter
@Getter
public class MessageRecord implements Serializable {



    /**
     * 主键
     */
    private Long id;

    /**
     * 发布状态
     * 0：发布成功
     * 1：发送中
     * 2：未能到达交换机
     * 3：未能到达队列
     */
    private String publishStatus;

    /**
     * 剩余重发次数
     */
    private int remainRetryCount;

    /**
     * 失败原因
     */
    private String cause;

    /**
     * 交换机名称
     */
    private String exchangeName;

    /**
     * 路由标识
     */
    private String rountingKey;

    /**
     * 消息
     */
    private String message;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date modifyDate;
}
