package com.hack2win.dynamicrabbits.mq.callback;

import com.hack2win.dynamicrabbits.constant.PublicStatusEnum;
import com.hack2win.dynamicrabbits.dynamic.entity.MessageRecord;
import com.hack2win.dynamicrabbits.dynamic.mapper.MessageRecordMapper;
import com.hack2win.dynamicrabbits.dynamic.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * 消息确认回调（确认是否到达交换机）
 */
@Component
public class RabbitComfirmCallback implements RabbitTemplate.ConfirmCallback {

    Logger log = LoggerFactory.getLogger(RabbitComfirmCallback.class);

    @Autowired
    MessageRecordMapper messageRecordMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId();
        //发送成功
        if (ack) {
            log.info("消息" + id + "成功投递至交换机");
            if (id.startsWith("retry_")) {
                //id以retry_开头则该消息为定时任务重推消息，成功后需更新数据库记录
                String messageRecordStr = stringRedisTemplate.opsForValue().get(id);
                if (StringUtils.isNoneBlank(messageRecordStr)) {
                    MessageRecord messageRecord = JsonUtils.readValue(messageRecordStr, MessageRecord.class);
                    if (messageRecord == null) {
                        return;
                    }

                    messageRecord.setModifyDate(new Date());
                    messageRecord.setRemainRetryCount(0);
                    messageRecord.setPublishStatus(PublicStatusEnum.SUCCESS.getCode());
                    messageRecordMapper.updateMessageRecord(messageRecord);
                }
            }
        } else {
            String messageRecordStr = stringRedisTemplate.opsForValue().get(id);
            if (StringUtils.isNoneBlank(messageRecordStr)) {
                MessageRecord messageRecord = JsonUtils.readValue(messageRecordStr, MessageRecord.class);
                if (messageRecord == null) {
                    return;
                }
                Date date = new Date();
                if (id.startsWith("retry_")) {
                    //id以retry_为开头，意味着该消息为定时重推消息，二次失败后需将重发次数-1
                    messageRecord.setCause(cause);
                    messageRecord.setModifyDate(date);
                    messageRecord.setRemainRetryCount(messageRecord.getRemainRetryCount() - 1);
                    messageRecord.setPublishStatus(PublicStatusEnum.NO_REACH_EXCHANGE.getCode());
                    messageRecordMapper.updateMessageRecord(messageRecord);
                } else {
                    //将失败的消息记录入库，留至定时任务重推
                    messageRecord.setCause(cause);
                    messageRecord.setCreateDate(date);
                    messageRecord.setModifyDate(date);
                    messageRecord.setPublishStatus(PublicStatusEnum.NO_REACH_EXCHANGE.getCode());
                    messageRecordMapper.insertMessageRecord(messageRecord);
                }
            }
        }
        stringRedisTemplate.delete(id);
    }
}
