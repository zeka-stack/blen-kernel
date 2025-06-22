package dev.dong4j.zeka.kernel.auth.context;

import dev.dong4j.zeka.kernel.auth.CurrentUser;

import java.io.Serializable;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.6.0
 */
public interface SecurityContext extends Serializable {
    /**
     * Gets authentication *
     *
     * @return the authentication
     * @since 1.6.0
     */
    CurrentUser getAuthentication();

    /**
     * Sets authentication *
     *
     * @param authentication authentication
     * @since 1.6.0
     */
    void setAuthentication(CurrentUser authentication);
}
