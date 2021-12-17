package com.hack2win.dynamicrabbits.dynamic.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hack2win.dynamicrabbits.cache.ConcurrentHashMapCacheUtils;
import com.hack2win.dynamicrabbits.dynamic.controller.StrategyController;
import com.hack2win.dynamicrabbits.dynamic.entity.Strategy;
import com.hack2win.dynamicrabbits.dynamic.mq.RabbitMQUtils;
import com.hack2win.dynamicrabbits.dynamic.redis.RedisTemplate;
import com.hack2win.dynamicrabbits.dynamic.service.IAccessService;
import com.hack2win.dynamicrabbits.dynamic.utils.JsonUtils;
import com.hack2win.dynamicrabbits.model.SendTopicVo;
import com.hack2win.dynamicrabbits.mq.service.MessagePublicService;
import com.rabbitmq.client.Channel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author DongerKai
 * @since 2021/12/11 11:34 ，1.0
 **/

@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements IAccessService {
    @NonNull private RedisTemplate redisTemplate;
    @NonNull private StringRedisTemplate stringRedisTemplate;
    @NonNull private MessagePublicService messagePublicService;
    @NonNull private StrategyController strategyController;

    @Resource
    private ConcurrentHashMapCacheUtils concurrentHashMapCacheUtils;
    @Override
    public String sendTopic(SendTopicVo sendTopic) {
        String topic = sendTopic.getTopic();
        // redis取数
        List<Strategy> valueFormatted = getQueueByTopic(topic);
        if (CollectionUtils.isEmpty(valueFormatted)) {
            return "empty topic";
        }
        List<String> queues = valueFormatted.stream().map(Strategy::getBindingQueue).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(queues)) {
            return "empty queue";
        }
        // 组装routingkey
        int retryCount = valueFormatted.get(0).getRemainRetryTimes();
        StringBuilder stringBuilder = new StringBuilder(topic);
        queues.forEach(row -> {

            stringBuilder.append(".").append(row);
        });
        String routingKey = stringBuilder.toString();
        messagePublicService.publicMessage(sendTopic.getExchangeName(), routingKey, sendTopic.getMessage(), retryCount);
        redisTemplate.updateCache();
        return "success";
    }

    private List<Strategy> getQueueByTopic(String topic) {
        List<Strategy> list = concurrentHashMapCacheUtils.getCache(topic);
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }
        String value = stringRedisTemplate.opsForValue().get(topic);
        if (StringUtils.isEmpty(value)) {
            // 调用接口缓存单条
            String strategy = strategyController.selectStrategyListByKey(topic);
            if (StringUtils.equals(strategy, "您没有配置服务映射表")) {
                return new ArrayList<>();
            }
            return JsonUtils.readValue(strategy, new TypeReference<List<Strategy>>(){});
        }
        return JsonUtils.readValue(value, new TypeReference<List<Strategy>>(){});
    }
}
