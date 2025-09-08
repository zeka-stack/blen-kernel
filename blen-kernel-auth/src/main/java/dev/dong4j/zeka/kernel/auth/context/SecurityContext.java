package dev.dong4j.zeka.kernel.auth.context;

import dev.dong4j.zeka.kernel.auth.CurrentUser;
import java.io.Serializable;

/**
 * 安全上下文接口，定义了管理当前用户认证信息的基本操作
 * 用于在不同线程间传递和访问用户认证状态，保证系统的安全性
 * 具体实现由SecurityContextImpl提供，通过SecurityContextHolder进行管理
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.0.0
 */
public interface SecurityContext extends Serializable {
    /**
     * Gets authentication *
     *
     * @return the authentication
     * @since 1.0.0
     */
    CurrentUser getAuthentication();

    /**
     * Sets authentication *
     *
     * @param authentication authentication
     * @since 1.0.0
     */
    void setAuthentication(CurrentUser authentication);
}
