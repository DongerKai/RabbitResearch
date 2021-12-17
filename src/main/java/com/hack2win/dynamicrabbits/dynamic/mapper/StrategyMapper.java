package com.hack2win.dynamicrabbits.dynamic.mapper;

import com.hack2win.dynamicrabbits.dynamic.entity.Strategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 策略接口
 */
@Mapper
public interface StrategyMapper {
    /**
     * 查询所有的策略信息
     *
     * @return 所有策略信息
     */
    List<Strategy> selectStrategyListById(@Param("topic") String topic);

    /**
     * 删除
     */
    int deleteStrategyById(@Param("topic") String topic,@Param("bindingQueue") String bindingQueue);

    /**
     * 插入
     */
    int insertStrategyById(Strategy strategy);

    /**
     * 修改
     */
    int updateStrategyById(Strategy strategy);
}
