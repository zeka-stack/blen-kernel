package dev.dong4j.zeka.kernel.devtools;

import lombok.Data;

/**
 * <p>Description: 模块配置 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:06
 * @since 1.0.0
 */
@Data
public class ModuleConfig {

    /** Module type */
    private ModuleType moduleType = ModuleType.SINGLE_MODULE;

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.27 18:06
     * @since 1.0.0
     */
    public enum ModuleType {
        /** 单模块 */
        SINGLE_MODULE,
        /** 多模块 */
        MULTI_MODULE

    }
}
