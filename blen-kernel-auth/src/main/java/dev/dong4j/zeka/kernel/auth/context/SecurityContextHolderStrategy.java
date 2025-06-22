package dev.dong4j.zeka.kernel.auth.context;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.6.0
 */
public interface SecurityContextHolderStrategy {

    /**
     * Clear context
     *
     * @since 1.6.0
     */
    void clearContext();

    /**
     * Gets context *
     *
     * @return the context
     * @since 1.6.0
     */
    SecurityContext getContext();

    /**
     * Sets context *
     *
     * @param context context
     * @since 1.6.0
     */
    void setContext(SecurityContext context);

    /**
     * Create empty context
     *
     * @return the security context
     * @since 1.6.0
     */
    SecurityContext createEmptyContext();
}
