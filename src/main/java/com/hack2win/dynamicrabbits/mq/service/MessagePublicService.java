package com.hack2win.dynamicrabbits.mq.service;

import com.hack2win.dynamicrabbits.dynamic.entity.MessageRecord;
import com.hack2win.dynamicrabbits.dynamic.redis.RedisTemplate;
import com.hack2win.dynamicrabbits.dynamic.utils.JsonUtils;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * 消息发布服务
 */
@Component
public class MessagePublicService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 发布消息
     * @param exchangeName 交换机名称
     * @param rountingKey 绑定标识
     * @param msg 消息
     * @param remainRetryCount 失败最大重发次数
     * @return
     */
    public void publicMessage(String exchangeName, String rountingKey, Object msg, int remainRetryCount) {
        this.publicMessage(exchangeName, rountingKey, msg, remainRetryCount, "rount_" + UUID.randomUUID().toString());
    }

    /**
     * 发布消息
     * @param exchangeName 交换机名称
     * @param rountingKey 绑定标识
     * @param msg 消息
     * @param remainRetryCount 失败最大重发次数
     * @param matchKey 缓存标识
     */
    public void publicMessage(String exchangeName, String rountingKey, Object msg, int remainRetryCount, String matchKey) {
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setExchangeName(exchangeName);
        messageRecord.setRountingKey(rountingKey);
        messageRecord.setRemainRetryCount(remainRetryCount);
        messageRecord.setMessage(JsonUtils.writeValueAsString(messageRecord));
        //将发布的信息放入缓存
        stringRedisTemplate.opsForValue().set(matchKey, JsonUtils.writeValueAsString(messageRecord));
        //发布mq
        rabbitTemplate.convertAndSend(exchangeName, rountingKey, JsonUtils.writeValueAsString(msg), new CorrelationData(matchKey));
    }

    /**
     * 发布消息
     * @param messageRecord 消息记录
     */
    public void publicMessage(MessageRecord messageRecord, String matchKey) {
        //将发布的信息放入缓存
        stringRedisTemplate.opsForValue().set(matchKey, JsonUtils.writeValueAsString(messageRecord));
        rabbitTemplate.convertAndSend(messageRecord.getExchangeName(), messageRecord.getRountingKey(), messageRecord.getMessage(), new CorrelationData(matchKey));
    }

    /**
     * 新增交换机
     * @param exchangeName 交换机名称
     */
    public void addExchange(String exchangeName) {
        rabbitAdmin.declareExchange(new TopicExchange(exchangeName));
    }

    /**
     * 新增队列
     * @param queueName 队列名称
     */
    public void addQueue(String queueName) {
        rabbitAdmin.declareQueue(new Queue(queueName));
    }

    /**
     * 新增绑定
     * @param exchangeName 交换机名称
     * @param queueName 队列名称
     * @param bindingKey 绑定标识
     */
    public void addBinding(String exchangeName, String queueName, String bindingKey) {
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue(queueName)).to(new TopicExchange(exchangeName)).with(bindingKey));
    }
}
