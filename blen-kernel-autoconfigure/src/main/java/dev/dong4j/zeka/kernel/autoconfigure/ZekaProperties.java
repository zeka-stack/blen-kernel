package dev.dong4j.zeka.kernel.autoconfigure;

import lombok.Getter;
import lombok.Setter;

/**
 * Zeka 组件通用属性配置类，定义了框架组件的通用配置项
 * <p>
 * 该类作为 Zeka 框架中所有组件的基础配置类，提供了统一的组件开关控制机制
 * 主要用于控制组件的全局启用状态，实现统一的配置管理和模块化控制
 * <p>
 * 主要特性：
 * - 提供通用的 enabled 开关属性
 * - 支持组件级别的启用/禁用控制
 * - 为所有 Zeka 组件提供统一的配置继承基类
 * - 默认开启所有功能，符合“约定大于配置”的设计理念
 * <p>
 * 使用方式：所有 Zeka 组件的配置类均可继承此类来实现统一的开关管理
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.06.25 01:29
 * @since 1.0.0
 */
@Getter
@Setter
public class ZekaProperties {
    /** 组件开关属性名常量 */
    public static final String ENABLED = "enabled";
    /** 开启状态常量值 */
    public static final String ON = "true";
    /** 组件全局可用状态，默认为开启 */
    private Boolean enabled = true;
}
