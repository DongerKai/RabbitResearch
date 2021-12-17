package com.hack2win.dynamicrabbits.mq.callback;

import com.hack2win.dynamicrabbits.constant.PublicStatusEnum;
import com.hack2win.dynamicrabbits.dynamic.entity.MessageRecord;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 队列消息确认回调（找不到正确路由时）
 */
@Component
public class RabbitReturnCallBack implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        //到达交换机，但未能进入到队列，向数据库插入一条记录
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setMessage(String.valueOf(message.getBody()));
        messageRecord.setCause(replyText);
        messageRecord.setRemainRetryCount(0);
        messageRecord.setExchangeName(exchange);
        messageRecord.setRountingKey(routingKey);
        Date date = new Date();
        messageRecord.setCreateDate(date);
        messageRecord.setModifyDate(date);
        messageRecord.setPublishStatus(PublicStatusEnum.NO_REACH_QUEUE.getCode());
    }
}
