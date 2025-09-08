package dev.dong4j.zeka.kernel.auth.context;

import dev.dong4j.zeka.kernel.auth.CurrentUser;
import java.io.Serial;

/**
 * 安全上下文的默认实现类，负责存储和管理当前用户的认证信息
 * 包含用户认证数据的存储、获取和比较操作
 * 支持序列化，可以在分布式系统中进行传输和存储
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.0.0
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
     * @since 1.0.0
     */
    public SecurityContextImpl() {

    }

    /**
     * Security context
     *
     * @param authentication authentication
     * @since 1.0.0
     */
    public SecurityContextImpl(CurrentUser authentication) {
        this.authentication = authentication;
    }

    /**
     * Equals
     *
     * @param obj obj
     * @return the boolean
     * @since 1.0.0
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
     * @since 1.0.0
     */
    @Override
    public CurrentUser getAuthentication() {
        return this.authentication;
    }

    /**
     * Hash code
     *
     * @return the int
     * @since 1.0.0
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
     * @since 1.0.0
     */
    @Override
    public void setAuthentication(CurrentUser authentication) {
        this.authentication = authentication;
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.0.0
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
