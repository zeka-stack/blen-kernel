package dev.dong4j.zeka.kernel.test;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.11.20 19:25
 * @since 2.1.0
 */
class SpringBootTestArgs implements ContextCustomizer {
    /** NO_ARGS */
    private static final String[] NO_ARGS = new String[0];
    /** Args */
    private final String[] args;

    /**
     * Spring boot test args
     *
     * @param testClass test class
     * @since 2.1.0
     */
    SpringBootTestArgs(Class<?> testClass) {
        this.args = MergedAnnotations.from(testClass,
                MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
            .get(SpringBootTest.class)
            .getValue("args", String[].class).orElse(NO_ARGS);
    }

    /**
     * Customize context
     *
     * @param context      context
     * @param mergedConfig merged config
     * @since 2.1.0
     */
    public void customizeContext(@NotNull ConfigurableApplicationContext context,
                                 @NotNull MergedContextConfiguration mergedConfig) {
    }

    /**
     * Get args
     *
     * @return the string [ ]
     * @since 2.1.0
     */
    String[] getArgs() {
        return this.args;
    }

    /**
     * Equals
     *
     * @param obj obj
     * @return the boolean
     * @since 2.1.0
     */
    public boolean equals(Object obj) {
        return obj != null && this.getClass() == obj.getClass() && Arrays.equals(this.args, ((SpringBootTestArgs) obj).args);
    }

    /**
     * Hash code
     *
     * @return the int
     * @since 2.1.0
     */
    public int hashCode() {
        return Arrays.hashCode(this.args);
    }

    /**
     * Get
     *
     * @param customizers customizers
     * @return the string [ ]
     * @since 2.1.0
     */
    static String[] get(@NotNull Set<ContextCustomizer> customizers) {
        Iterator<ContextCustomizer> customizerIterator = customizers.iterator();

        ContextCustomizer customizer;
        do {
            if (!customizerIterator.hasNext()) {
                return NO_ARGS;
            }

            customizer = customizerIterator.next();
        } while (!(customizer instanceof SpringBootTestArgs));

        return ((SpringBootTestArgs) customizer).args;
    }
}
