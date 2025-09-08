package dev.dong4j.zeka.kernel.autoconfigure.util;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import dev.dong4j.zeka.kernel.common.util.JustOnceLogger;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 自动配置跳过工具类，用于动态禁用指定的Spring Boot自动配置类
 * 通过修改系统属性来排除不需要的自动配置，避免配置冲突和不必要的组件加载
 * 主要用于组件间的优先级控制和条件化配置管理
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
     * 跳过指定的自动配置类，将其添加到排除列表中
     *
     * @param className  调用者类名，用于日志输出
     * @param configName 需要跳过的配置类名
     * @since 1.0.0
     */
    public void skip(String className, String configName) {
        JustOnceLogger.warnOnce(className, StringUtils.format("默认的 {} 已被禁用", configName));
        String property = System.getProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE);
        String value;
        if (StringUtils.isBlank(property)) {
            value = configName;

        } else {
            value = String.join(",",
                property,
                configName);
        }
        System.setProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE, value);
    }
}
