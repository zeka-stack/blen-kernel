package dev.dong4j.zeka.kernel.auth.context;

import org.springframework.util.Assert;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.6.0
 */
final class ThreadLocalSecurityContextHolderStrategy implements
    SecurityContextHolderStrategy {

    /** CONTEXT_HOLDER */
    private static final ThreadLocal<SecurityContext> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * Clear context
     *
     * @since 1.6.0
     */
    @Override
    public void clearContext() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * Gets context *
     *
     * @return the context
     * @since 1.6.0
     */
    @Override
    public SecurityContext getContext() {
        SecurityContext ctx = CONTEXT_HOLDER.get();

        if (ctx == null) {
            ctx = this.createEmptyContext();
            CONTEXT_HOLDER.set(ctx);
        }

        return ctx;
    }

    /**
     * Sets context *
     *
     * @param context context
     * @since 1.6.0
     */
    @Override
    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        CONTEXT_HOLDER.set(context);
    }

    /**
     * Create empty context
     *
     * @return the security context
     * @since 1.6.0
     */
    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
