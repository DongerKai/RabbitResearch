package com.hack2win.dynamicrabbits.dynamic.redis;

import com.alibaba.fastjson.JSONObject;
import com.hack2win.dynamicrabbits.dynamic.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * TODO
 *
 * @author DongerKai
 * @since 2021/12/11 14:57 ，1.0
 **/
@Component
@Slf4j
@AllArgsConstructor
public class RedisTemplate {

    private StringRedisTemplate stringRedisTemplate;
    public void updateCache() {
        List<String> queues = Arrays.asList("queue1","queue2");
        stringRedisTemplate.opsForValue().set("key-topic", JsonUtils.writeValueAsString(queues));
    }

    /**
     * 查询json的数据
     *
     * @param key 入参
     * @return key对应的value值
     */
    public String queryCacheByKey(String key) {


        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 更新数据
     *
     * @param jsonObject json数据
     */
    public void updateCacheByJson(JSONObject jsonObject) {
        Set<String> strings = jsonObject.keySet();
        strings.forEach((r) -> {
            stringRedisTemplate.opsForValue().set(r, JsonUtils.writeValueAsString(jsonObject.get(r)));
        });
    }

    public void updateCacheByJson(String topic, String value) {
        stringRedisTemplate.opsForValue().set(topic, value);
    }

    /**
     * 删除key
     *
     * @param routingKey key
     */
    public void deleteCacheByJson(String routingKey) {
        stringRedisTemplate.opsForValue().decrement(routingKey);
    }
}