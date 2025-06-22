package dev.dong4j.zeka.kernel.autoconfigure.util;

import dev.dong4j.zeka.kernel.common.constant.ConfigKey;
import dev.dong4j.zeka.kernel.common.util.JustOnceLogger;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2022.01.08 14:21
 * @since 2022.1.1
 */
@Slf4j
@UtilityClass
public class SkipAutoCinfiguration {

    /**
     * Skip
     *
     * @param className  class name
     * @param configName config name
     * @since 2022.1.1
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
