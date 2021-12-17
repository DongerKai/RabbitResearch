package com.hack2win.dynamicrabbits.dynamic.service;

import com.hack2win.dynamicrabbits.dynamic.entity.Strategy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 策略接口
 */
//@Service("strategyService")
public interface StrategyService {
    /**
     * 查询所有的策略信息
     *
     * @return 所有策略信息
     */
    List<Strategy> selectStrategyListById(String routingKey);


    /**
     * 删除
     */
    int deleteStrategyByKey(String topic,String queue);

    /**
     * 插入
     */
    int insertStrategyById(Strategy strategy);

    /**
     * 修改
     */
    int updateStrategyById(Strategy strategy);
}
