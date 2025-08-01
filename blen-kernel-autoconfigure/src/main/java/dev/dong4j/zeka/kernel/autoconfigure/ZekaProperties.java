package dev.dong4j.zeka.kernel.autoconfigure;

import lombok.Getter;
import lombok.Setter;

/**
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
