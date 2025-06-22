package dev.dong4j.zeka.kernel.common.bundle;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Description: 国际化配置文件动态绑定基类, 所有的业务端都继承此类实现自己的业务配置</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.19 11:22
 * @since 1.4.0
 */
@Slf4j
public abstract class DynamicBundle extends AbstractBundle {

    /** INSTANCE */
    public static final DynamicBundle INSTANCE = new DynamicBundle("") {
    };

    /**
     * Dynamic bundle
     *
     * @param pathToBundle path to bundle
     * @since 1.4.0
     */
    protected DynamicBundle(@NotNull String pathToBundle) {
        super(pathToBundle);
    }

    /**
     * Returns the class this method was called 'framesToSkip' frames up the caller hierarchy.
     * <p>
     * NOTE:
     * <b>Extremely expensive!
     * Please consider not using it.
     * These aren't the droids you're looking for!</b>
     *
     * @param framesToSkip frames to skip
     * @return the class
     * @since 1.4.0
     */
    @Nullable
    public static Class<?> findCallerClass(int framesToSkip) {
        try {
            Class<?>[] stack = MySecurityManager.INSTANCE.getStack();
            int indexFromTop = 1 + framesToSkip;
            return stack.length > indexFromTop ? stack[indexFromTop] : null;
        } catch (Exception e) {
            log.warn("", e);
            return null;
        }
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.05.19 11:22
     * @since 1.4.0
     */
    private static final class MySecurityManager extends SecurityManager {
        /** INSTANCE */
        private static final MySecurityManager INSTANCE = new MySecurityManager();

        /**
         * Get stack
         *
         * @return the class [ ]
         * @since 1.4.0
         */
        Class<?>[] getStack() {
            return this.getClassContext();
        }
    }
}
