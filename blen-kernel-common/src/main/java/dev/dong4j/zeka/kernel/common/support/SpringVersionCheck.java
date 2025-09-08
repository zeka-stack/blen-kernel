package dev.dong4j.zeka.kernel.common.support;

import dev.dong4j.zeka.kernel.common.exception.ServiceInternalException;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.springframework.core.SpringVersion;

/**
 * <p>Description: spring 版本检查 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.20 21:58
 * @since 1.0.0
 */
@UtilityClass
public final class SpringVersionCheck {

    /** VERSION */
    private static final String VERSION = SpringVersion.getVersion();
    /** SPRING_VERSION_4 */
    private static final String SPRING_VERSION_4 = "4";

    static {
        if (StringUtils.isNotBlank(VERSION) && VERSION.startsWith(SPRING_VERSION_4)) {
            throw new ServiceInternalException("The zeka-stack only supports Spring 5.x.");
        }
    }

    /**
     * Version
     *
     * @return the string
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static String version() {
        return VERSION;
    }
}
