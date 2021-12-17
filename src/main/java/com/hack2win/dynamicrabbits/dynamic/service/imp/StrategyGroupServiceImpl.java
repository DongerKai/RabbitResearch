package com.hack2win.dynamicrabbits.dynamic.service.imp;

import com.hack2win.dynamicrabbits.dynamic.entity.SystemGroup;
import com.hack2win.dynamicrabbits.dynamic.mapper.StrategyGroupMapper;
import com.hack2win.dynamicrabbits.dynamic.service.StrategyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyGroupServiceImpl implements StrategyGroupService {
    @Autowired
    private StrategyGroupMapper strategyGroupMapper;

    @Override
    public List<SystemGroup> selectSystemGroupListById() {
        return strategyGroupMapper.selectSystemGroupListById();
    }

    @Override
    public int deleteSystemGroupById(Integer id) {

        return strategyGroupMapper.deleteSystemGroupById(id);
    }

    @Override
    public int insertSystemGroupById(SystemGroup systemGroup) {
        return strategyGroupMapper.insertSystemGroupById(systemGroup);
    }

    @Override
    public int updateSystemGroupById(SystemGroup systemGroup) {
        return strategyGroupMapper.updateSystemGroupById(systemGroup);
    }
}
