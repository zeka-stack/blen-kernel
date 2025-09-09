package dev.dong4j.zeka.kernel.autoconfigure.util;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import dev.dong4j.zeka.kernel.common.util.JustOnceLogger;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>自动配置跳过工具类.
 * <p>用于动态禁用指定的 Spring Boot 自动配置类.
 * <p>通过修改系统属性来排除不需要的自动配置，避免配置冲突和不必要的组件加载.
 * <p>实现原理：
 * <ul>
 *     <li>获取当前 spring.autoconfigure.exclude 系统属性</li>
 *     <li>将新的配置类名添加到排除列表中</li>
 *     <li>更新系统属性，使 Spring Boot 跳过指定的自动配置</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>组件间的优先级控制</li>
 *     <li>条件化配置管理</li>
 *     <li>避免默认自动配置的冲突</li>
 *     <li>按需加载组件</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2022.01.08 14:21
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class SkipAutoCinfiguration {

    /**
     * <p>跳过指定的自动配置类.
     * <p>将指定的配置类添加到 Spring Boot 的自动配置排除列表中.
     * <p>处理逻辑：
     * <ol>
     *     <li>记录禁用日志（仅记录一次）</li>
     *     <li>获取当前排除列表</li>
     *     <li>将新的配置类添加到列表中</li>
     *     <li>更新系统属性</li>
     * </ol>
     *
     * @param className  调用者类名，用于日志输出和唯一性控制
     * @param configName 需要跳过的自动配置类的全限定名称
     * @since 1.0.0
     */
    public void skip(String className, String configName) {
        // 使用 JustOnceLogger 记录禁用日志，避免重复输出
        JustOnceLogger.warnOnce(className, StringUtils.format("默认的 {} 已被禁用", configName));
        // 获取当前的 spring.autoconfigure.exclude 系统属性
        String property = System.getProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE);
        String value;
        if (StringUtils.isBlank(property)) {
            // 如果排除列表为空，直接设置为当前配置类
            value = configName;
        } else {
            // 如果排除列表不为空，将当前配置类添加到列表末尾
            value = String.join(",", property, configName);
        }
        // 更新系统属性，使 Spring Boot 跳过指定的自动配置类
        System.setProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE, value);
    }
}
