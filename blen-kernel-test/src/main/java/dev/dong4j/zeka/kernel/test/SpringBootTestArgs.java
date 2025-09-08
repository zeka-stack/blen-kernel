package dev.dong4j.zeka.kernel.test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

/**
 * <p>Description: </p>
 *
 * @param args Args
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.11.20 19:25
 * @since 1.0.0
 */
record SpringBootTestArgs(String[] args) implements ContextCustomizer {
    /** NO_ARGS */
    private static final String[] NO_ARGS = new String[0];

    /**
     * Spring boot test args
     *
     * @param args test class
     * @since 1.0.0
     */
    SpringBootTestArgs(Class<?> args) {
        this.args = MergedAnnotations.from(args,
                MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
            .get(SpringBootTest.class)
            .getValue("args", String[].class).orElse(NO_ARGS);
    }

    /**
     * Customize context
     *
     * @param context      context
     * @param mergedConfig merged config
     * @since 1.0.0
     */
    public void customizeContext(@NotNull ConfigurableApplicationContext context,
                                 @NotNull MergedContextConfiguration mergedConfig) {
    }

    /**
     * Get args
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    @Override
    public String[] args() {
        return this.args;
    }

    /**
     * Equals
     *
     * @param obj obj
     * @return the boolean
     * @since 1.0.0
     */
    public boolean equals(Object obj) {
        return obj != null && this.getClass() == obj.getClass() && Arrays.equals(this.args, ((SpringBootTestArgs) obj).args);
    }

    /**
     * Hash code
     *
     * @return the int
     * @since 1.0.0
     */
    public int hashCode() {
        return Arrays.hashCode(this.args);
    }

    /**
     * Get
     *
     * @param customizers customizers
     * @return the string [ ]
     * @since 1.0.0
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
