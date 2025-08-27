package dev.dong4j.zeka.kernel.auth.context;

import dev.dong4j.zeka.kernel.auth.CurrentUser;
import java.io.Serial;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.6.0
 */
public class SecurityContextImpl implements SecurityContext {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -2828643112455883285L;

    /** Authentication */
    private CurrentUser authentication;

    /**
     * Security context
     *
     * @since 1.6.0
     */
    public SecurityContextImpl() {

    }

    /**
     * Security context
     *
     * @param authentication authentication
     * @since 1.6.0
     */
    public SecurityContextImpl(CurrentUser authentication) {
        this.authentication = authentication;
    }

    /**
     * Equals
     *
     * @param obj obj
     * @return the boolean
     * @since 1.6.0
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SecurityContextImpl test) {

            if ((this.getAuthentication() == null) && (test.getAuthentication() == null)) {
                return true;
            }

            return (this.getAuthentication() != null) && (test.getAuthentication() != null)
                && this.getAuthentication().equals(test.getAuthentication());
        }

        return false;
    }

    /**
     * Gets authentication *
     *
     * @return the authentication
     * @since 1.6.0
     */
    @Override
    public CurrentUser getAuthentication() {
        return this.authentication;
    }

    /**
     * Hash code
     *
     * @return the int
     * @since 1.6.0
     */
    @Override
    public int hashCode() {
        if (this.authentication == null) {
            return -1;
        } else {
            return this.authentication.hashCode();
        }
    }

    /**
     * Sets authentication *
     *
     * @param authentication authentication
     * @since 1.6.0
     */
    @Override
    public void setAuthentication(CurrentUser authentication) {
        this.authentication = authentication;
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.6.0
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        if (this.authentication == null) {
            sb.append(": Null authentication");
        } else {
            sb.append(": Authentication: ").append(this.authentication);
        }

        return sb.toString();
    }
}
