package com.hack2win.dynamicrabbits.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author DongerKai
 * @since 2021/12/11 22:13 ，1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SendBatchTopicVo {
    private String count;
    private String exchangeName;
    private String msg;
    private String topic;

}
