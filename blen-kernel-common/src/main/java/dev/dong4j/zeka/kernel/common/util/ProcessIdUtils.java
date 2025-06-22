package dev.dong4j.zeka.kernel.common.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * <p>Description: 获取进程的 pid</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.04 19:22
 * @since 1.4.0
 */
@UtilityClass
public class ProcessIdUtils {

    /** DEFAULT_PROCESSID */
    public static final String DEFAULT_PROCESSID = "-";

    /**
     * Gets process id *
     *
     * @return the process id
     * @since 1.5.0
     */
    public static String getProcessId() {
        try {
            Class<?> managementFactoryClass = Class.forName("java.lang.management.ManagementFactory");
            Method mxBean = managementFactoryClass.getDeclaredMethod("getRuntimeMXBean");
            Class<?> forName = Class.forName("java.lang.management.RuntimeMXBean");
            Method getName = forName.getDeclaredMethod("getName");

            Object invoke = mxBean.invoke(null);
            String name = (String) getName.invoke(invoke);
            return name.split("@")[0];
        } catch (Exception ex) {
            try {
                return new File("/proc/self").getCanonicalFile().getName();
            } catch (IOException ignored) {
                // nothing to do
            }
        }
        return DEFAULT_PROCESSID;
    }
}
