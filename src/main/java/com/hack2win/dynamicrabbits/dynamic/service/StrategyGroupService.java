package com.hack2win.dynamicrabbits.dynamic.service;

import com.hack2win.dynamicrabbits.dynamic.entity.SystemGroup;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 策略接口
 */
//@Service("strategyGroupService")
public interface StrategyGroupService {
    /**
     * 查询所有的应用信息
     *
     * @return 所有策略信息
     */
    List<SystemGroup> selectSystemGroupListById();


    /**
     * 删除
     */
    int deleteSystemGroupById(Integer id);

    /**
     * 插入
     */
    int insertSystemGroupById(SystemGroup SystemGroup);

    /**
     * 修改
     */
    int updateSystemGroupById(SystemGroup SystemGroup);
}
