package com.hack2win.dynamicrabbits.dynamic.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hack2win.dynamicrabbits.constant.ResponseConstant;
import com.hack2win.dynamicrabbits.dynamic.entity.Strategy;
import com.hack2win.dynamicrabbits.dynamic.redis.RedisTemplate;
import com.hack2win.dynamicrabbits.dynamic.service.StrategyService;
import com.hack2win.dynamicrabbits.dynamic.utils.JsonUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 策略配置
 */

@RestController
@AllArgsConstructor
@RequestMapping("/strategy")
public class StrategyController {
    @Resource
    private StrategyService strategyservice;

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 查询服务配置 redis缓存
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/select/{routingKey}")
    public String selectStrategyListByKey(@PathVariable("routingKey") String routingKey) {
        if (StringUtils.isBlank(routingKey)) {
            List<Strategy> strategies = strategyservice.selectStrategyListById(routingKey.trim());
            return JsonUtils.writeValueAsString(strategies);
        }
        String result = redisTemplate.queryCacheByKey(routingKey);
        if (StringUtils.isBlank(result)) {
            synchronized (this) {
                result = redisTemplate.queryCacheByKey(routingKey);
                if (StringUtils.isBlank(result)) {
                    List<Strategy> strategies = strategyservice.selectStrategyListById(routingKey);
                    if (CollectionUtils.isEmpty(strategies)) {
                        return "您没有配置服务映射表";
                    }
                    // 插入所有数据
                    String value = JsonUtils.writeValueAsString(strategies);
                    redisTemplate.updateCacheByJson(routingKey, value);
                    return value;
                }
            }
        }
        return result;
    }

    /**
     * 插入服务信息
     *
     * @return
     */
    @PostMapping("/insert")
    public int insertStrategyById(@RequestBody Strategy strategy) {
        Strategy strategyNew = new Strategy();
        strategyNew.setDescription(strategy.getDescription());
        strategyNew.setRoutingName(strategy.getRoutingName());
        strategyNew.setBindingQueue(strategy.getBindingQueue());
        strategyNew.setTopic(strategy.getBindingQueue());
        int row = strategyservice.insertStrategyById(strategy);
        updateRedisCache(strategy);
        return row;
    }

    /**
     * 修改服务信息
     *
     * @return
     */
    @PostMapping("/update")
    public int updateStrategyById(@RequestBody Strategy strategy) {
        Strategy strategyUpdate = new Strategy();
        strategyUpdate.setId(strategy.getId());
        strategyUpdate.setDescription(strategy.getDescription());
        strategyUpdate.setRoutingName(strategy.getRoutingName());
        strategyUpdate.setBindingQueue(strategy.getBindingQueue());
        strategyUpdate.setTopic(strategy.getBindingQueue());
        int row = strategyservice.updateStrategyById(strategy);
        updateRedisCache(strategy);
        return row;
    }

    /**
     * 删除服务信息
     *
     * @return
     */
    @PostMapping("/delete")
    public int deleteStrategyById(@RequestBody Strategy strategy) {
        int row = strategyservice.deleteStrategyByKey(strategy.getTopic(),strategy.getBindingQueue());
        updateRedisCache(strategy);
        return row;
    }

    private void updateRedisCache(Strategy strategy) {
        List<Strategy> strategies = strategyservice.selectStrategyListById(strategy.getTopic());
        if (CollectionUtils.isEmpty(strategies)){
            redisTemplate.deleteCacheByJson(strategy.getTopic());
        }
        redisTemplate.updateCacheByJson(strategy.getTopic(),JsonUtils.writeValueAsString(strategies));
    }

}
