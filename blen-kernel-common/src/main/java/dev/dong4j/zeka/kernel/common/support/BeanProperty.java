package dev.dong4j.zeka.kernel.common.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: Bean属性 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:10
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public class BeanProperty {
    /** Name */
    private final String name;
    /** Type */
    private final Class<?> type;
}
