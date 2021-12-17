package com.hack2win.dynamicrabbits.dynamic.controller;

import com.alibaba.fastjson.JSONObject;
import com.hack2win.dynamicrabbits.dynamic.entity.SystemGroup;
import com.hack2win.dynamicrabbits.dynamic.service.StrategyGroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应用服务配置
 */
@Controller
@RequestMapping(("/strategygroup"))
public class StrategyGroupController {
    @Resource
    private StrategyGroupService strategyGroupService;

    /**
     * 查询服务配置
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/select")
    public String selectSystemGroupListById(Model model) {
        // 查询信息 id
        List<SystemGroup> strategies = strategyGroupService.selectSystemGroupListById();
        System.out.println("=======" + strategies);

        return JSONObject.toJSON(strategies).toString();
    }

    /**
     * 插入服务信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/insert")
    public String insertStrategyById() {
//    public String insertStrategyById(@RequestParam("strategy") SystemGroup strategy) {

        SystemGroup strategy = new SystemGroup();
        strategy.setDescription("薪酬指定消费的数据");
        strategy.setSystemName("薪酬1号");
        strategy.setSystemInfo("C");
        int row = strategyGroupService.insertSystemGroupById(strategy);
        if (row > 0) {
            return "插入数据成功";
        }
        return "插入数失败";
    }

    /**
     * 修改服务信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public String updateStrategyById() {
//    public String updateStrategyById(@RequestParam("strategy") Strategy strategy) {
        SystemGroup strategy = new SystemGroup();
        strategy.setId(2);
        strategy.setDescription("张三");
        strategy.setSystemName("薪酬2号");
        strategy.setSystemInfo("B");
        int row = strategyGroupService.updateSystemGroupById(strategy);
        if (row > 0) {
            return "修改数据" + strategy.getSystemName() + "成功";
        }
        return "修改数据" + strategy.getSystemName() + "失败";
    }

    /**
     * 删除服务信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete/{id}")
    public String deleteStrategyById(@PathVariable("id") String id) {

        int row = strategyGroupService.deleteSystemGroupById(Integer.parseInt(id));
        if (row > 0) {
            return "删除数据" + id + "成功";
        }
        return "删除数据" + id + "失败";
    }


}
