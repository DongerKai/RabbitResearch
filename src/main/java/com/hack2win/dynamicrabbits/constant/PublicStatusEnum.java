package com.hack2win.dynamicrabbits.constant;

/**
 * 消息发布状态枚举类
 */
public enum PublicStatusEnum {

    SUCCESS("0", "发布成功"),TRANSMITTING("1", "传送中"), NO_REACH_EXCHANGE("2", "未能到达交换机"), NO_REACH_QUEUE("3", "未能到达队列")
    ;
    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    PublicStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
