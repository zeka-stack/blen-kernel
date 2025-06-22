package dev.dong4j.zeka.kernel.spi.extension.support;

import dev.dong4j.zeka.kernel.spi.extension.Activate;
import dev.dong4j.zeka.kernel.spi.extension.SPI;
import dev.dong4j.zeka.kernel.spi.extension.SPILoader;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Comparator;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@SuppressWarnings("all")
public class ActivateComparator implements Comparator<Object> {

    /** COMPARATOR */
    public static final Comparator<Object> COMPARATOR = new ActivateComparator();

    /**
     * Compare
     *
     * @param o1 o 1
     * @param o2 o 2
     * @return the int
     * @since 1.8.0
     */
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1.equals(o2)) {
            return 0;
        }

        Class<?> inf = findSpi(o1.getClass());

        ActivateInfo a1 = parseActivate(o1.getClass());
        ActivateInfo a2 = parseActivate(o2.getClass());

        if ((a1.applicableToCompare() || a2.applicableToCompare()) && inf != null) {
            SPILoader<?> strategyLoader = SPILoader.getExtensionLoader(inf);
            if (a1.applicableToCompare()) {
                String n2 = strategyLoader.getExtensionName(o2.getClass());
                if (a1.isLess(n2)) {
                    return -1;
                }

                if (a1.isMore(n2)) {
                    return 1;
                }
            }

            if (a2.applicableToCompare()) {
                String n1 = strategyLoader.getExtensionName(o1.getClass());
                if (a2.isLess(n1)) {
                    return 1;
                }

                if (a2.isMore(n1)) {
                    return -1;
                }
            }
        }
        int n1 = a1 == null ? 0 : a1.order;
        int n2 = a2 == null ? 0 : a2.order;
        // never return 0 even if n1 equals n2, otherwise, o1 and o2 will override each other in collection like HashSet
        return n1 > n2 ? 1 : -1;
    }

    /**
     * Find spi
     *
     * @param clazz clazz
     * @return the class
     * @since 1.8.0
     */
    private Class<?> findSpi(Class clazz) {
        if (clazz.getInterfaces().length == 0) {
            return null;
        }

        for (Class<?> intf : clazz.getInterfaces()) {
            if (intf.isAnnotationPresent(SPI.class)) {
                return intf;
            } else {
                Class result = findSpi(intf);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * Parse activate
     *
     * @param clazz clazz
     * @return the activate info
     * @since 1.8.0
     */
    private ActivateInfo parseActivate(Class<?> clazz) {
        ActivateInfo info = new ActivateInfo();
        if (clazz.isAnnotationPresent(Activate.class)) {
            Activate activate = clazz.getAnnotation(Activate.class);
            info.before = activate.before();
            info.after = activate.after();
            info.order = activate.order();
        } else {
            Activate activate = clazz.getAnnotation(Activate.class);
            info.before = activate.before();
            info.after = activate.after();
            info.order = activate.order();
        }
        return info;
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.8.0
     * @email "mailto:dong4j@gmaidl.com"
     * @date 2021.02.26 17:47
     * @since 1.8.0
     */
    private static class ActivateInfo {
        /** Before */
        private String[] before;
        /** After */
        private String[] after;
        /** Order */
        private int order;

        /**
         * Applicable to compare
         *
         * @return the boolean
         * @since 1.8.0
         */
        private boolean applicableToCompare() {
            return ArrayUtils.isNotEmpty(before) || ArrayUtils.isNotEmpty(after);
        }

        /**
         * Is less
         *
         * @param name name
         * @return the boolean
         * @since 1.8.0
         */
        private boolean isLess(String name) {
            return Arrays.asList(before).contains(name);
        }

        /**
         * Is more
         *
         * @param name name
         * @return the boolean
         * @since 1.8.0
         */
        private boolean isMore(String name) {
            return Arrays.asList(after).contains(name);
        }
    }
}
