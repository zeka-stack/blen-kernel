package dev.dong4j.zeka.kernel.autoconfigure;

import lombok.Getter;
import lombok.Setter;

/**
 * Zeka组件通用属性配置类，定义了框架组件的通用配置项
 * 主要用于控制组件的全局启用状态，支持统一的开关控制
 * 所有Zeka组件均可继承此类来实现统一的开关管理
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.06.25 01:29
 * @since x.x.x
 */
@Getter
@Setter
public class ZekaProperties {
    public static final String ENABLED = "enabled";
    public static final String ON = "true";
    /** 组件全局可用状态 */
    private Boolean enabled = true;
}
