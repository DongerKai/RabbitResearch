package com.hack2win.dynamicrabbits.dynamic.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统应用配置
 */
@Data
@Setter
@Getter
public class SystemGroup {
    private Integer id;
    /**
     * 系统名称 systemname
     */
    private String systemName;
    /**
     * 系统信息
     */
    private String systemInfo;
    /**
     * 系统信息
     */
    private String description;

    /**
     * 是否删除
     */
    private String isDelete;

    /**
     * 是否启用
     */
    private String isEnable;
}
