package dev.dong4j.zeka.kernel.devtools;

import lombok.Data;

/**
 * 模块配置类，用于定义代码生成器的模块类型和相关配置
 * <p>
 * 支持单模块和多模块两种项目结构类型
 * 根据不同的模块类型生成不同的项目结构和代码模式
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:06
 * @since 1.0.0
 */
@Data
public class ModuleConfig {

    /** 模块类型，默认为单模块类型 */
    private ModuleType moduleType = ModuleType.SINGLE_MODULE;

    /**
     * 模块类型枚举，定义项目的组织结构类型
     * <p>
     * 支持单模块和多模块两种项目结构
     * 影响代码生成的包结构和Converter的生成策略
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.27 18:06
     * @since 1.0.0
     */
    public enum ModuleType {
        /** 单模块类型，适用于简单的单体应用 */
        SINGLE_MODULE,
        /** 多模块类型，适用于复杂的企业级应用和分层架构 */
        MULTI_MODULE

    }
}
