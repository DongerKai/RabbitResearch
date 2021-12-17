package com.hack2win.dynamicrabbits.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * TODO
 *
 * @author DongerKai
 * @since 2021/12/11 14:03 ，1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SendTopicVo {
    @NotBlank(message = "topic不能为空！")
    private String topic;

    @NotBlank(message = "消息不能为空!")
    private String message;

    @NotBlank(message = "exchangeName不能为空!")
    private String exchangeName;
}
