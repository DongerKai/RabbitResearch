package com.hack2win.dynamicrabbits.dynamic.service.imp;

import com.hack2win.dynamicrabbits.dynamic.entity.Strategy;
import com.hack2win.dynamicrabbits.dynamic.mapper.StrategyMapper;
import com.hack2win.dynamicrabbits.dynamic.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyServiceImpl implements StrategyService {
    @Autowired
    private StrategyMapper strategyMapper;


    @Override
    public List<Strategy> selectStrategyListById(String routingKey) {
        return strategyMapper.selectStrategyListById(routingKey);
    }

    @Override
    public int deleteStrategyByKey(String routingKey, String queue) {
        return strategyMapper.deleteStrategyById(routingKey,queue);
    }

    @Override
    public int insertStrategyById(Strategy strategy) {
        return strategyMapper.insertStrategyById(strategy);
    }

    @Override
    public int updateStrategyById(Strategy strategy) {
        return strategyMapper.updateStrategyById(strategy);
    }
}
