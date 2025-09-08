package dev.dong4j.zeka.kernel.common.support;

import lombok.AllArgsConstructor;

/**
 * <p>Description: Bean属性 </p>
 *
 * @param name Name
 * @param type Type
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:10
 * @since 1.0.0
 */
@AllArgsConstructor
public record BeanProperty(String name, Class<?> type) {
}
